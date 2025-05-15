import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sqlalchemy import create_engine
import mysql.connector
import joblib

# 1. Load and preprocess training data (using only 5% of the dataset)
df = pd.read_csv(
    r'C:\Users\Nadine Ziedi\Downloads\HateSpeechClassification\data\HateSpeechDatasetBalanced.csv',
    header=None, names=['Content', 'Label'], dtype=str, low_memory=False
)

# Sample 5% of the dataset
df_sample = df.sample(frac=0.15, random_state=42)  # This takes 5% of the data
X = df_sample['Content']
y = df_sample['Label']

# 2. Vectorize the text
vectorizer = TfidfVectorizer(max_features=1000)
X_vectorized = vectorizer.fit_transform(X)

# 3. Train/test split and model training
X_train, X_test, y_train, y_test = train_test_split(
    X_vectorized, y, test_size=0.2, random_state=42
)
model = RandomForestClassifier(n_estimators=100, random_state=42, n_jobs=-1)  # Parallelize with n_jobs=-1
model.fit(X_train, y_train)

# 4. Fetch messages from your MySQL DB
engine = create_engine('mysql+mysqlconnector://root:@localhost:3307/CommunicationConstructify')
df_mysql = pd.read_sql("SELECT message_id, content FROM message", engine)

# 5. Preprocess and predict
messages_vectorized = vectorizer.transform(df_mysql['content'])
predictions = model.predict(messages_vectorized)

# 6. Update DB with predictions
df_mysql['isHateSpeech'] = predictions.astype(int)  # Convert to int before bool()

# 7. Connect to MySQL and update rows
conn = mysql.connector.connect(
    host="localhost",
    port=3307,
    database="CommunicationConstructify",
    user="root",
    password=""
)
cursor = conn.cursor()

for idx, row in df_mysql.iterrows():
    cursor.execute(
        "UPDATE message SET is_hate_speech = %s WHERE message_id = %s",
        (bool(row['isHateSpeech']), int(row['message_id']))
    )

conn.commit()
cursor.close()
conn.close()


joblib.dump(model, 'model.pkl')
joblib.dump(vectorizer, 'vectorizer.pkl')