# Stage 1: Build dependencies
FROM public.ecr.aws/lambda/python:3.11 AS build

WORKDIR /build

# Copy only requirements and install dependencies directly into a clean folder
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt -t /build/python

# Stage 2: Final image with only runtime dependencies
FROM public.ecr.aws/lambda/python:3.11

WORKDIR /var/task

# Copy all installed Python packages from the build image
COPY --from=build /build/python /var/task/

# Copy your source code into the image
COPY . /var/task/

# Lambda entry point
CMD ["app.lambda_handler.lambda_handler"]