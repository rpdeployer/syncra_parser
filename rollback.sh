#!/bin/bash

ENV_FILE=".env"

if [ ! -f "$ENV_FILE" ]; then
  echo "Файл $ENV_FILE не найден!"
  exit 1
fi

# читаем текущую версию
CURRENT_VERSION=$(grep PARSER_VERSION "$ENV_FILE" | cut -d '=' -f2)

echo "Текущая версия PARSER: $CURRENT_VERSION"
echo -n "Введите новую версию: "
read NEW_VERSION

if [ -z "$NEW_VERSION" ]; then
  echo "Версия не указана. Отмена."
  exit 1
fi

# обновляем .env
sed -i "s/^PARSER_VERSION=.*/PARSER_VERSION=$NEW_VERSION/" "$ENV_FILE"

echo "Обновлён $ENV_FILE:"
grep PARSER_VERSION "$ENV_FILE"

docker compose down -v
docker compose up -d parser

echo "Готово! parser теперь работает на версии $NEW_VERSION"