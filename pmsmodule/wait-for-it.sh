#!/bin/bash
set -e

host="$1"
port="$2"

until nc -z "$host" "$port"; do
  echo "Waiting for SQL Server at $host:$port..."
  sleep 10
done

exec "$@"
