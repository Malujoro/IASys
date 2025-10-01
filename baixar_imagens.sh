# --- 1) Baixar escudos dos times ---
TIMES_DIR="messageGenerator/images/times"
mkdir -p "$TIMES_DIR"

while read url; do
  filename=$(basename "$url")   # extrai o nome do arquivo, ex: corinthians.png
  filepath="$TIMES_DIR/$filename"

  if [ -f "$filepath" ]; then
    echo "[INFO] Escudo '$filename' já existe, pulando download."
  else
    echo "[INFO] Baixando escudo '$filename'..."
    curl -s -o "$filepath" "$url"
  fi
done < times.txt

# --- 2) Baixar dataset de emoções (Kaggle) ---
DATASET_DIR="messageGenerator/images/pessoas"
DATASET_ZIP="$DATASET_DIR/emotion-dataset.zip"

mkdir -p "$DATASET_DIR"

if [ -d "$DATASET_DIR/emotion-dataset" ]; then
  echo "[INFO] Dataset já descompactado em $DATASET_DIR/emotion-dataset, pulando download."
elif [ -f "$DATASET_ZIP" ]; then
  echo "[INFO] Arquivo ZIP já existe, apenas descompactando..."
  unzip -o "$DATASET_ZIP" -d "$DATASET_DIR"
else
  echo "[INFO] Baixando dataset de emoções do Kaggle..."
  curl -L -o "$DATASET_ZIP" \
    https://www.kaggle.com/api/v1/datasets/download/abdallahwagih/emotion-dataset
  unzip -o "$DATASET_ZIP" -d "$DATASET_DIR"
fi
