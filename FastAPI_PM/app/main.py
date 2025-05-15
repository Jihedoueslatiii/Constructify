from fastapi import FastAPI

# from .config import MODEL_PATH, SCALER_PATH
import pickle
import warnings
import pandas as pd
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from datetime import date

warnings.filterwarnings("ignore")
#FastAPI app
app = FastAPI()

#You can change here to implement more security
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


class PredictionRequest(BaseModel):
    project_theme: str
    project_type: str
    budget: int
    team_size: int
    contractor_quality: str
    project_manager_level: str
    start_date: date
    end_date: date
    
    
def load_pkl(pkl_path: str):
    with open(pkl_path, 'rb') as f:
        model = pickle.load(f)
    return model


model = load_pkl('model/xgb_model.pkl')
scaler = load_pkl('model/scaler/budget_scaler.pkl') 
category_encoder = load_pkl('model/encoder/category_encoder.pkl')
project_theme_encoder = load_pkl('model/encoder/project_theme_encoder.pkl')
project_type_encoder = load_pkl('model/encoder/project_type_encoder.pkl')

df_th_cat = pd.read_csv("model/theme_category_mapping.csv")
theme_to_category = pd.Series(df_th_cat.category.values, index=df_th_cat.project_theme).to_dict()

manager_level_order = {'Junior': 0, 'Mid-level': 1, 'Senior': 2}
contractor_quality_order = {'A+': 4, 'A': 3, 'B': 2, 'C': 1, 'D': 0}


def prepare_data(project_theme, project_type , category, budget ,start_date , end_date , team_size , contractor_quality, project_manager_level):
    

    project_theme_encoded = project_theme_encoder.transform([[project_theme]]).tolist()[0]
    cateogry_encoded = category_encoder.transform([[category]]).tolist()[0]
    
    project_type_encoded = project_type_encoder.transform([[project_type]]).toarray()
    project_type_encoded = project_type_encoded[0].tolist()
    
    budget_scaled = scaler.transform([[budget]])[0][0]
    duration_days = (end_date - start_date).days
    start_month = start_date.month
    end_month = end_date.month
    start_year = start_date.year
    end_year = end_date.year
    
    contractor_quality_mapped = contractor_quality_order[contractor_quality]
    project_manager_level_mapped = manager_level_order[project_manager_level]
    
    prepared_data = [project_theme_encoded,cateogry_encoded,budget_scaled,team_size,contractor_quality_mapped,project_manager_level_mapped,duration_days
                     ,start_month,end_month,start_year,end_year]
    prepared_data.extend(project_type_encoded)
    
    return prepared_data



@app.get("/")
def read_root():
    return {"message": "Hello World"}


@app.post("/predict")
def predict(data: PredictionRequest):
    project_theme = data.project_theme
    project_type = data.project_type
    category = theme_to_category.get(project_theme)
    budget = data.budget
    team_size = data.team_size
    contractor_quality = data.contractor_quality
    project_manager_level = data.project_manager_level
    start_date = data.start_date
    end_date = data.end_date
    
    pre_data = prepare_data(project_theme,project_type, category, budget,start_date,end_date,team_size,contractor_quality,project_manager_level)

    num_tasks = model.predict([pre_data])
    num_tasks = round(num_tasks[0])

    return {"prediction": num_tasks}


