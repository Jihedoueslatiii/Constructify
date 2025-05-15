from flask import Flask, request, jsonify
import json
import ollama
import datetime
from flask_cors import CORS



app = Flask(__name__)
CORS(app)



MODEL_NAME = "llama3.2" 

def format_messages_for_summarization(messages_data):
    active_messages = [msg for msg in messages_data if not msg.get('isDeleted', False)]
    active_messages.sort(key=lambda x: x.get('timestamp', ''))
    
    sender_map = {}
    for msg in active_messages:
        sender_id = msg.get('senderId')
        if sender_id and sender_id not in sender_map:
            sender_map[sender_id] = f"Person {len(sender_map) + 1}"
    
    formatted_messages = []
    
    formatted_messages.append({
        "role": "system",
        "content": "You are a helpful assistant that summarizes conversations. Provide a concise summary of the following conversation."
    })
    
    for msg in active_messages:
        sender_id = msg.get('senderId')
        sender_name = sender_map.get(sender_id, "Unknown")
        content = msg.get('content', '')
        
        timestamp = msg.get('timestamp', '')
        if timestamp:
            try:
                # Convert timestamp to string if it's not already
                if not isinstance(timestamp, str):
                    timestamp = str(timestamp)
                dt = datetime.datetime.fromisoformat(timestamp)
                formatted_time = dt.strftime("%Y-%m-%d %H:%M")
            except (ValueError, TypeError) as e:
                print(f"Error parsing timestamp '{timestamp}': {str(e)}")
                formatted_time = "unknown time"
        else:
            formatted_time = "unknown time"
        
        formatted_content = f"{sender_name} ({formatted_time}): {content}"
        
        formatted_messages.append({
            "role": "user" if len(formatted_messages) % 2 == 1 else "assistant",
            "content": formatted_content
        })
    
    formatted_messages.append({
        "role": "user",
        "content": "Please provide a concise summary of this conversation. Include key discussion points, decisions made, and any action items mentioned."
    })
    
    return formatted_messages

def generate_summary(conversation_data):
    messages = format_messages_for_summarization(conversation_data)
    
    try:
        response = ollama.chat(
            model=MODEL_NAME,
            messages=messages,
            stream=False
        )
        
        summary = response['message']['content']
        return summary
    
    except Exception as e:
        print(f"Error calling Ollama: {str(e)}")
        return f"Error generating summary: {str(e)}"

@app.route('/summarize', methods=['POST'])
def summarize_conversation():
    """API endpoint to summarize a conversation"""
    data = request.json
    
    if not data:
        return jsonify({"error": "Missing request data"}), 400
    
    if 'messages' in data:
        messages = data['messages']
    elif 'conversation_id' in data and 'user_id' in data:
        return jsonify({"error": "Direct API integration not implemented. Please provide messages directly."}), 501
    else:
        return jsonify({"error": "Missing 'messages' or 'conversation_id'/'user_id' fields"}), 400
    
    summary = generate_summary(messages)
    
    return jsonify({"summary": summary})

@app.route('/health', methods=['GET'])
def health_check():
    """Simple health check endpoint"""
    return jsonify({"status": "healthy", "model": MODEL_NAME})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=6000, debug=True)

