#!/bin/bash

curl "https://api.whaleclub.co/v1/position/new" \
  -H "Authorization: Bearer ab7d8bd3-98b5-438a-8d38-0247ee9578d0" \
  -X POST \
  -d 'direction=long' \
  -d 'market=BTC-USD' \
  -d 'leverage=1' \
  -d 'size=100000000'
