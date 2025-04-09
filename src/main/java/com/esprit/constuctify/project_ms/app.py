from flask import Flask, request, jsonify
import pandas as pd
from sklearn.preprocessing import LabelEncoder
from sklearn.neighbors import NearestNeighbors
import joblib

app = Flask(__name__)

# Load the dataset
data = pd.read_csv("/content/Project%20Management%20%282%29.csv")

# Rename columns to match entity attributes
data = data.rename(columns={
    "id_projet": "idProjet",
    "id_task": "idTask",
    "nom_tache": "title",
    "etat_tache": "status",
    "priority": "priority",
    "project_name": "nomProjet",
    "description": "description",
    "due_date": "dueDate",
    "date_debut": "dateDebut",
    "date_fin": "dateFin",
    "etat_projet": "etatProjet"
})

# Prepare data for modeling (Label Encoding)
encoder = LabelEncoder()
data["type_projet"] = encoder.fit_transform(data["nomProjet"])
data["status"] = encoder.fit_transform(data["status"])
data["priority"] = encoder.fit_transform(data["priority"])

# Convert dates to numerical values
data["dueDate"] = (pd.to_datetime(data["dueDate"]) - pd.Timestamp("1970-01-01")).dt.days
data["dateDebut"] = (pd.to_datetime(data["dateDebut"]) - pd.Timestamp("1970-01-01")).dt.days
data["dateFin"] = (pd.to_datetime(data["dateFin"]) - pd.Timestamp("1970-01-01")).dt.days

# Define the features for KNN
features = data[["type_projet", "status", "priority", "dueDate", "dateDebut", "dateFin"]]

# Fit KNN model
knn = NearestNeighbors(n_neighbors=3, metric='euclidean')
knn.fit(features)

# Save the encoder and model for later use
joblib.dump(encoder, 'encoder.pkl')
joblib.dump(knn, 'knn_model.pkl')

# Recommendation function for tasks by project name
@app.route('/recommend_tasks_by_project', methods=['POST'])
def recommend_tasks_by_project():
    input_data = request.get_json()
    project_name_input = input_data.get('project_name')

    if project_name_input not in data["nomProjet"].values:
        return jsonify({"error": "Invalid project name. Please check the available project names."}), 400

    project_data = data[data["nomProjet"] == project_name_input]
    recommended_tasks = project_data[["idTask", "title", "status", "priority", "dueDate"]].to_dict(orient='records')
    return jsonify({"recommended_tasks": recommended_tasks})

# Recommendation function for tasks by project type, status, and priority
@app.route('/recommend_tasks', methods=['POST'])
def recommend_tasks():
    input_data = request.get_json()
    project_type_input = input_data.get('project_type')
    task_status_input = input_data.get('task_status')
    priority_input = input_data.get('priority')

    try:
        input_data_encoded = pd.DataFrame([[project_type_input, task_status_input, priority_input]], columns=["type_projet", "status", "priority"])
        input_data_encoded["type_projet"] = encoder.transform([project_type_input])
        input_data_encoded["status"] = encoder.transform([task_status_input])
        input_data_encoded["priority"] = encoder.transform([priority_input])

        distances, indices = knn.kneighbors(input_data_encoded)
        recommendations = data.iloc[indices.flatten()][["idTask", "title", "status", "priority", "dueDate"]].to_dict(orient='records')
        return jsonify({"recommended_tasks": recommendations})
    except ValueError as e:
        return jsonify({"error": str(e)}), 400

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)