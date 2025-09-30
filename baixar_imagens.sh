mkdir -p messageGenerator/baixar_imagens.sh/imagens/times
while read url; do
  curl -O --output-dir messageGenerator/baixar_imagens.sh/imagens/times "$url"
done < imagens.txt
