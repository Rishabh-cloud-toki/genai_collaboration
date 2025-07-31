const express = require('express');

const app = express();
const port = process.env.PORT || 3000;

app.use(express.json());

let users = [];
let tasks = [];
let nextUserId = 1;
let nextTaskId = 1;
// For testing: reset in-memory data
app.reset = () => {
  users.length = 0;
  tasks.length = 0;
  nextUserId = 1;
  nextTaskId = 1;
};

// User endpoints
app.get('/users', (req, res) => {
  res.json(users);
});

app.get('/users/:id', (req, res) => {
  const id = parseInt(req.params.id, 10);
  const user = users.find(u => u.id === id);
  if (!user) {
    return res.status(404).json({ error: 'User not found' });
  }
  res.json(user);
});

app.post('/users', (req, res) => {
  const { name } = req.body;
  if (!name) {
    return res.status(400).json({ error: 'Name is required' });
  }
  const user = { id: nextUserId++, name };
  users.push(user);
  res.status(201).json(user);
});

app.put('/users/:id', (req, res) => {
  const id = parseInt(req.params.id, 10);
  const { name } = req.body;
  if (!name) {
    return res.status(400).json({ error: 'Name is required' });
  }
  const user = users.find(u => u.id === id);
  if (!user) {
    return res.status(404).json({ error: 'User not found' });
  }
  user.name = name;
  res.json(user);
});

app.delete('/users/:id', (req, res) => {
  const id = parseInt(req.params.id, 10);
  const index = users.findIndex(u => u.id === id);
  if (index === -1) {
    return res.status(404).json({ error: 'User not found' });
  }
  users.splice(index, 1);
  res.status(204).send();
});

// Task endpoints
app.get('/tasks', (req, res) => {
  const { assignedTo } = req.query;
  let result = tasks;
  if (assignedTo) {
    const userId = parseInt(assignedTo, 10);
    result = tasks.filter(t => t.assignedTo === userId);
  }
  res.json(result);
});

app.get('/tasks/:id', (req, res) => {
  const id = parseInt(req.params.id, 10);
  const task = tasks.find(t => t.id === id);
  if (!task) {
    return res.status(404).json({ error: 'Task not found' });
  }
  res.json(task);
});

app.post('/tasks', (req, res) => {
  const { title, description, assignedTo, priority } = req.body;
  if (!title || !assignedTo) {
    return res.status(400).json({ error: 'Title and assignedTo are required' });
  }
  const userId = parseInt(assignedTo, 10);
  const user = users.find(u => u.id === userId);
  if (!user) {
    return res.status(400).json({ error: 'Assigned user does not exist' });
  }
  const task = {
    id: nextTaskId++,
    title,
    description: description || '',
    assignedTo: userId,
    priority: priority || 'medium',
    createdAt: new Date().toISOString()
  };
  tasks.push(task);
  res.status(201).json(task);
});

app.put('/tasks/:id', (req, res) => {
  const id = parseInt(req.params.id, 10);
  const task = tasks.find(t => t.id === id);
  if (!task) {
    return res.status(404).json({ error: 'Task not found' });
  }
  const { title, description, assignedTo, priority } = req.body;
  if (assignedTo) {
    const userId = parseInt(assignedTo, 10);
    const user = users.find(u => u.id === userId);
    if (!user) {
      return res.status(400).json({ error: 'Assigned user does not exist' });
    }
    task.assignedTo = userId;
  }
  if (title) task.title = title;
  if (description) task.description = description;
  if (priority) task.priority = priority;
  res.json(task);
});

app.delete('/tasks/:id', (req, res) => {
  const id = parseInt(req.params.id, 10);
  const index = tasks.findIndex(t => t.id === id);
  if (index === -1) {
    return res.status(404).json({ error: 'Task not found' });
  }
  tasks.splice(index, 1);
  res.status(204).send();
});

if (require.main === module) {
  app.listen(port, () => {
    console.log(`Buckets API listening at http://localhost:${port}`);
  });
}

module.exports = app;