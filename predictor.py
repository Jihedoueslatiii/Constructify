import pandas as pd
import numpy as np
from sqlalchemy import create_engine
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier
from sklearn.svm import SVC
from sklearn.impute import SimpleImputer
from sklearn.metrics import classification_report
from fpdf import FPDF
import os
import matplotlib.pyplot as plt
import seaborn as sns
from io import BytesIO
import tempfile

def lire_csv_avec_delimiteur(file_path, sep=';'):
    """
    Fonction pour lire un fichier CSV avec un délimiteur personnalisé.
    """
    try:
        return pd.read_csv(file_path, sep=sep)
    except Exception as e:
        print(f"Erreur lors de la lecture du fichier CSV : {e}")
        return None

def clean_data(df):
    """
    Fonction pour nettoyer les données, traiter les valeurs manquantes et bruitées.
    """
    colonnes_numeriques = ['cost', 'budget', 'other_expenses', 'roi']
    df[colonnes_numeriques] = df[colonnes_numeriques].replace(r'[^0-9.-]', np.nan, regex=True)
    valeurs_manquantes = df[colonnes_numeriques].isna().sum().sum()
    print(f"Valeurs manquantes/bruitées : {valeurs_manquantes}")
    
    # Imputer les valeurs manquantes avec la moyenne
    imputer = SimpleImputer(strategy='mean')
    df[colonnes_numeriques] = imputer.fit_transform(df[colonnes_numeriques])
    
    return df, int(valeurs_manquantes)

def train_models():
    """
    Fonction pour entraîner les modèles de classification DecisionTree et SVM.
    """
    df = lire_csv_avec_delimiteur("Budget.csv", sep=';')
    if df is None:
        return None, None, None, None, None

    df['depasse_budget_Tree'] = ((df['cost'] + df['other_expenses']) > (df['budget'] + df['roi'])).astype(int)
    df, valeurs_manquantes = clean_data(df)

    X = df[['cost', 'budget', 'other_expenses', 'roi']]
    y = df['depasse_budget_Tree']
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    # Entraîner Decision Tree
    dt_model = DecisionTreeClassifier(random_state=0)
    dt_model.fit(X_train, y_train)

    # Entraîner SVM
    svm_model = SVC(probability=True)
    svm_model.fit(X_train, y_train)

    # Évaluer les modèles
    dt_report = classification_report(y_test, dt_model.predict(X_test), output_dict=True)
    svm_report = classification_report(y_test, svm_model.predict(X_test), output_dict=True)

   #  print("Évaluation du DecisionTree :\n", dt_report)
    # print("Évaluation du SVM :\n", svm_report)

    return dt_model, svm_model, dt_report, svm_report, valeurs_manquantes

def charger_donnees_mysql():
    """
    Fonction pour charger les données depuis la base de données MySQL.
    """
    try:
        engine = create_engine('mysql+mysqlconnector://root:@localhost:3307/Finance')
        query = "SELECT finance_id, cost, budget, other_expenses, roi, description FROM finance"
        return pd.read_sql(query, con=engine)
    except Exception as e:
        print(f"Erreur MySQL : {e}")
        return None

def save_report_image(report_data, filename):
    """
    Fonction pour générer une image du rapport de classification sous forme de heatmap.
    """
    plt.figure(figsize=(6, 4))
    sns.heatmap(report_data, annot=True, cmap='Blues', fmt='.2f', cbar=False)
    plt.title(f"Classification Report - {filename}")

    # Enregistrer l'image dans un fichier temporaire
    temp_file = tempfile.NamedTemporaryFile(delete=False, suffix='.png')
    plt.savefig(temp_file, format='png')
    plt.close()
    temp_file.close()
    return temp_file.name  # Retourner le chemin du fichier temporaire

def export_to_pdf(projets, filename, dt_report, svm_report, valeurs_manquantes):
    """
    Fonction pour exporter les résultats et rapports sous forme de PDF.
    """
    pdf = FPDF()
    pdf.add_page()
    pdf.set_font("helvetica", "B", 14)
    pdf.cell(200, 10, txt="Rapport de prédiction de dépassement de budget", align='C')

    pdf.set_font("helvetica", size=12)
    pdf.cell(0, 10, txt=f"Valeurs manquantes détectées et traitées : {valeurs_manquantes}", ln=True)

    pdf.set_font("helvetica", "B", 12)
    pdf.cell(0, 10, txt="Statistiques du modèle Decision Tree :", ln=True)
    pdf.set_font("helvetica", size=10)

    # Enregistrer l'image du rapport du Decision Tree
    dt_img_path = save_report_image(pd.DataFrame(dt_report).iloc[:-1, :-1], 'DecisionTree')
    pdf.image(dt_img_path, x=10, y=60, w=180)

    pdf.set_font("helvetica", "B", 12)
    pdf.cell(0, 10, txt="Statistiques du modèle SVM :", ln=True)
    pdf.set_font("helvetica", size=10)

    # Enregistrer l'image du rapport SVM
    svm_img_path = save_report_image(pd.DataFrame(svm_report).iloc[:-1, :-1], 'SVM')
    pdf.image(svm_img_path, x=10, y=150, w=180)

    pdf.set_font("helvetica", "B", 12)
    pdf.cell(0, 10, txt="Projets prédits comme dépassant le budget :", ln=True)
    pdf.set_font("helvetica", size=10)

    for _, row in projets.iterrows():
        texte = (f"ID: {row['finance_id']} | Desc: {row.get('description', 'N/A')} | "
                 f"Coût: {row['cost']} | Budget: {row['budget']} | Excès (SVM): {row['depasse_budget_pred_SVM']} | "
                 f"Excès (TREE): {row['depasse_budget_Tree']}") 
        pdf.multi_cell(0, 5, texte)

    pdf.output(filename)
    print(f" Rapport PDF généré : {filename}")

def predict_models():
    """
    Fonction principale pour effectuer la prédiction et générer le rapport.
    """
    dt_model, svm_model, dt_report, svm_report, valeurs_manquantes = train_models()
    if dt_model is None or svm_model is None:
        print("Erreur d'entraînement.")
        return

    # Charger les données depuis MySQL
    df_mysql = charger_donnees_mysql()
    if df_mysql is None:
        print("Erreur de chargement MySQL.")
        return

    print("\n Données depuis MySQL :")
    print(df_mysql)

    # Nettoyer les données et prédire les dépassements de budget
    df_clean, _ = clean_data(df_mysql.copy())
    df_clean['description'] = df_mysql['description']  # Restaurer les descriptions

    df_clean['depasse_budget_pred_SVM'] = dt_model.predict(df_clean[['cost', 'budget', 'other_expenses', 'roi']])
    df_clean['depasse_budget_Tree'] = ((df_clean['cost'] + df_clean['other_expenses']) > (df_clean['budget'] + df_clean['roi'])).astype(int)

    projets_export = df_clean[df_clean['depasse_budget_pred_SVM'] == 1]

    print("\n Projets qui vont dépasser le budget (prédiction) :")
    print(projets_export)

    # Exporter le rapport en PDF
    export_to_pdf(projets_export, "depassement_budget.pdf", dt_report, svm_report, valeurs_manquantes)

if __name__ == "__main__":
    predict_models()
