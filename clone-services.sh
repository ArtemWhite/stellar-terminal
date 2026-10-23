#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

clone_if_missing() {
  local name="$1"
  local dest="$2"
  local url="https://github.com/ArtemWhite/${name}.git"
  if [ -d "${dest}/.git" ]; then
    echo "Пропускаю ${name}: уже склонирован в ${dest}"
  else
    # убираем пустую оболочку, оставшуюся после git rm -r
    rm -rf "${dest}"
    echo "Клонирую ${name} → ${dest}"
    git clone "${url}" "${dest}"
  fi
}

mkdir -p server

clone_if_missing "catalog-service"   "server/catalog-service"
clone_if_missing "satellite-service" "server/satellite-service"
clone_if_missing "user-service"      "server/user-service"
clone_if_missing "shared"            "shared"

echo "Готово. Теперь собирай: ./gradlew :client:build"
