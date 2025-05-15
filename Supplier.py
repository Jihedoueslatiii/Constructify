import pandas as pd
import numpy as np
from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.cluster import KMeans
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline
import mysql.connector
import json
import warnings

# Suppress warnings to avoid polluting stdout
warnings.filterwarnings("ignore")

# === 1. Load training data ===
train_df = pd.read_csv("C:\\Users\\Nadine Ziedi\\Downloads\\back_Nadinneeeeeeeeeeeee\\back\\supplier.csv")


# === 2. Define columns ===
categorical_cols = ['industry', 'status']
numerical_cols = ['reliability_score', 'total_contract_value']

# === 3. Clean training data ===
train_df.dropna(subset=categorical_cols + numerical_cols, inplace=True)

for col in numerical_cols:
    train_df = train_df[pd.to_numeric(train_df[col], errors='coerce').notnull()]
    train_df[col] = pd.to_numeric(train_df[col])

# === 4. Preprocessing + KMeans Pipeline ===
preprocessor = ColumnTransformer([
    ('num', StandardScaler(), numerical_cols),
    ('cat', OneHotEncoder(handle_unknown='ignore'), categorical_cols)
])

kmeans_pipeline = Pipeline([
    ('preprocess', preprocessor),
    ('kmeans', KMeans(n_clusters=3, random_state=42))
])

# === 5. Train the model ===
kmeans_pipeline.fit(train_df)

# === 6. Connect to MySQL and load test data ===
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="",
    port=3307,
    database="constructify"
)

query = "SELECT industry, status, reliability_score, total_contract_value FROM supplier"
test_df = pd.read_sql(query, conn)
conn.close()

# === 7. Clean test data ===
for col in numerical_cols:
    test_df = test_df[pd.to_numeric(test_df[col], errors='coerce').notnull()]
    test_df[col] = pd.to_numeric(test_df[col])

test_df.dropna(subset=categorical_cols + numerical_cols, inplace=True)

# === 8. Predict clusters ===
test_df['cluster'] = kmeans_pipeline.predict(test_df)

# === 9. Return JSON only ===
print(json.dumps(test_df.to_dict(orient="records")))