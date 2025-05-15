# -*- coding: utf-8 -*-
import sys
import io
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
import pandas as pd
import numpy as np
from sklearn.neighbors import NearestNeighbors
from sklearn.preprocessing import StandardScaler
from sqlalchemy import create_engine

# === 1. Chargement du train_set depuis le CSV avec le bon séparateur ===
df_train = pd.read_csv("C:\\Users\\Nadine Ziedi\\Downloads\\back_Nadinneeeeeeeeeeeee\\back\\Deliverable1_clean.csv", sep=';', encoding='utf-8')


# === 2. Connexion à MySQL pour le test_set ===
db_url = "mysql+pymysql://root:@localhost:3307/Deliverabledb"
engine = create_engine(db_url)
query = "SELECT * FROM deliverable"
df_test = pd.read_sql(query, engine)

# === 3. Nettoyage des colonnes textuelles ===
def clean_text_columns(df, text_columns):
    for col in text_columns:
        if col in df.columns:
            df[col] = df[col].astype(str).str.strip()
            df[col] = df[col].str.replace(r'\s+', ' ', regex=True)
            df[col] = df[col].str.replace(r'[^\w\s.,-]', '', regex=True)
            df[col] = df[col].replace('nan', np.nan)
    return df

text_cols_to_clean = ['name', 'status', 'Responsable', 'Priorite', 'Commentaires']
df_train = clean_text_columns(df_train, text_cols_to_clean)
df_test = clean_text_columns(df_test, text_cols_to_clean)

# === 4. Préparation des features numériques ===
features = ['cout_estime']

# Conversion explicite en float (texte sera converti en NaN)
df_train['cout_estime'] = pd.to_numeric(df_train['cout_estime'], errors='coerce')
df_test['cout_estime'] = pd.to_numeric(df_test['cout_estime'], errors='coerce')

# Remplacer les NaN par 0
df_train[features] = df_train[features].fillna(0)
df_test[features] = df_test[features].fillna(0)

# === 5. Normalisation ===
scaler = StandardScaler()
X_train = scaler.fit_transform(df_train[features])
X_test = scaler.transform(df_test[features])

# === 6. Modèle KNN ===
model = NearestNeighbors(n_neighbors=3, algorithm='auto')
model.fit(X_train)

# === 7. Générer des recommandations ===
for idx, x in enumerate(X_test):
    distances, indices = model.kneighbors([x])
    print(f"\n   Recommandations pour : {df_test.iloc[idx].get('name', 'Inconnu')}")
    for i in indices[0]:
        reco_name = df_train.iloc[i].get('name') or df_train.iloc[i].get('Nom du livrable')
        print(f" -   {reco_name} (coût estimé : {df_train.iloc[i]['cout_estime']})")

print("\n  Recommandations générées avec succès.")

