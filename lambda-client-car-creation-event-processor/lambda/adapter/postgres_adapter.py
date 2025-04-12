import os
import psycopg2
from utils.logger import logger

DB_HOST = os.getenv("DB_HOST")
DB_NAME = os.getenv("DB_NAME")
DB_USER = os.getenv("DB_USER")
DB_PASSWORD = os.getenv("DB_PASSWORD")

def get_db_connection():
    try:
        return psycopg2.connect(
            dbname=DB_NAME,
            user=DB_USER,
            password=DB_PASSWORD,
            host=DB_HOST
        )
    except Exception as e:
        logger.error(f"Database connection error: {e}")
        raise

def save_to_database(table, column, value):
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        query = f"INSERT INTO {table} ({column}) VALUES (%s) ON CONFLICT DO NOTHING;"
        cursor.execute(query, (value,))
        
        conn.commit()
        cursor.close()
        conn.close()
        
        logger.info(f"Successfully inserted {column}={value} into {table} table")
    
    except Exception as e:
        logger.error(f"Database insert error for {table}: {e}")
        raise