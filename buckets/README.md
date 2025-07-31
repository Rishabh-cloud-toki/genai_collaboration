# Buckets

A simple Node.js + Express API for collaborative task assignment.

## Setup

Install dependencies:

```bash
npm install
```

Start the server:

```bash
npm start
```

The API will be running at http://localhost:3000.

## API Endpoints

### Users

- GET /users
- GET /users/:id
- POST /users { name }
- PUT /users/:id { name }
- DELETE /users/:id

### Tasks

- GET /tasks?assignedTo=ID
- GET /tasks/:id
- POST /tasks { title, description, assignedTo, priority }
- PUT /tasks/:id { title, description, assignedTo, priority }
- DELETE /tasks/:id