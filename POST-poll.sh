curl -X POST http://localhost:8080/api/polls \
-H "Content-Type: application/json" \
-d '{
  "question": "What is your favorite programming language?",
  "publishedAt": "2024-10-18T10:30:00Z",
  "validUntil": "2024-10-25T10:30:00Z",
  "voteOptions": []
}'

