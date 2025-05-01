from app.adapter.car_api_gateway import get_car_by_vin
from app.adapter.dtos.car_response import CarResponse

def fetch_car_info(vin: str) -> dict:
    car: CarResponse = get_car_by_vin(vin)
    return {
        "model": car.model,
        "modelYear": car.modelYear,
        "manufacturer": car.manufacturer,
        "color": car.color,
        "vin": car.vin,
        "value": float(car.value) if car.value is not None else None
    }