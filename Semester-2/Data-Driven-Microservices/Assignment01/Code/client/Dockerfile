FROM python:3-stretch
WORKDIR /app
COPY . /app
RUN python -m pip install --upgrade pip
RUN pip install -r requirements.txt
CMD ["python", "client.py"]