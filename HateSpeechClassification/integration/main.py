from flask import Flask, request, jsonify
import joblib
import os
import logging
import re
import string
from flask_cors import CORS

# Create Flask app
app = Flask(__name__)
CORS(app)

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Load model and vectorizer
MODEL_PATH = os.path.join(os.path.dirname(__file__), '../model.pkl')
VECTORIZER_PATH = os.path.join(os.path.dirname(__file__), '../vectorizer.pkl')

# Check if files exist
if not os.path.exists(MODEL_PATH):
    logger.error(f"Model file not found: {MODEL_PATH}")
    raise FileNotFoundError(f"Model file not found: {MODEL_PATH}")

if not os.path.exists(VECTORIZER_PATH):
    logger.error(f"Vectorizer file not found: {VECTORIZER_PATH}")
    raise FileNotFoundError(f"Vectorizer file not found: {VECTORIZER_PATH}")

# Load model and vectorizer
model = joblib.load(MODEL_PATH)
vectorizer = joblib.load(VECTORIZER_PATH)

# Preprocess text to ensure consistency between training and prediction
def preprocess_text(text):
    """Preprocess the text by lowering case, removing punctuation, and extra spaces."""
    # Lowercase the text
    text = text.lower()
    # Remove punctuation
    text = text.translate(str.maketrans('', '', string.punctuation))
    # Remove extra spaces
    text = ' '.join(text.split())
    return text

@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.get_json()

        if not data or 'content' not in data:
            return jsonify({'error': 'Missing "content" in request'}), 400

        content = data['content']
        processed_content = preprocess_text(content)

        vectorized = vectorizer.transform([processed_content])

        # 🔍 Debug line to check if vectorization worked
        print("Non-zero features:", vectorized.nnz)

        # Make the prediction
        prediction = model.predict(vectorized)[0]
        print(f"Prediction: {prediction}")  # Print the raw prediction for debugging

        return jsonify({
    'content': content,
    'isHateSpeech': bool(int(prediction))  # Ensures it's a clean Python bool: True or False
})


    except Exception as e:
        logger.error(f"Error during prediction: {str(e)}")
        return jsonify({'error': 'Prediction failed', 'message': str(e)}), 500

# Run the app
if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
