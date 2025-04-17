import os
import psycopg
from utils.logger import logger

DB_HOST = os.getenv("DB_HOST")
DB_NAME = os.getenv("DB_NAME")
DB_USER = os.getenv("DB_USER")
DB_PASSWORD = os.getenv("DB_PASSWORD")
DB_PORT = os.getenv("DB_PORT")

def get_db_connection():
    try:
        return psycopg.connect(
            dbname=DB_NAME,
            user=DB_USER,
            password=DB_PASSWORD,
            host=DB_HOST,
            port=DB_PORT,
        )
    except Exception as e:
        logger.error(f"Database connection error: {e}")
        raise

def save_to_database(table, column, value):
    try:
        with get_db_connection() as conn:
            with conn.cursor() as cursor:
                query = f"INSERT INTO {table} ({column}) VALUES (%s) ON CONFLICT DO NOTHING;"
                cursor.execute(query, (value,))
                conn.commit()
                logger.info(f"Successfully inserted {column}={value} into {table} table")

    except Exception as e:
        logger.error(f"Database insert error for {table}: {e}")
        raise