
from fastapi import FastAPI, UploadFile, File, HTTPException
from PIL import Image, UnidentifiedImageError
from io import BytesIO

app = FastAPI()

@app.get("/health")
def health():
    return {"status": "healthy"}

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    print("Received filename:", file.filename)
    print("Received content type:", file.content_type)

    if not file.filename:
        raise HTTPException(status_code=400, detail="Filename is missing")

    contents = await file.read()

    if not contents:
        raise HTTPException(status_code=400, detail="Uploaded file is empty")

    try:
        image = Image.open(BytesIO(contents))
        image.verify()
    except (UnidentifiedImageError, OSError, ValueError):
        raise HTTPException(
            status_code=400,
            detail="Uploaded file is not a valid image"
        )

    return {
        "status": "success",
        "filename": file.filename,
        "prediction": "NOT_AVAILABLE",
        "confidence": None,
        "message": "Image received and validated successfully."
    }
