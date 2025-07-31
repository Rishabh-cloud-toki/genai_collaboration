const request = require('supertest');
const { expect } = require('chai');
const app = require('../index');

describe('Buckets API', () => {
  beforeEach(() => {
    app.reset();
  });

  describe('User endpoints', () => {
    it('should return empty array for GET /users', async () => {
      const res = await request(app).get('/users');
      expect(res.status).to.equal(200);
      expect(res.body).to.be.an('array').that.is.empty;
    });

    it('should return 400 when POST /users without name', async () => {
      const res = await request(app)
        .post('/users')
        .send({});
      expect(res.status).to.equal(400);
      expect(res.body).to.have.property('error', 'Name is required');
    });

    it('should create a user with POST /users', async () => {
      const res = await request(app)
        .post('/users')
        .send({ name: 'Alice' });
      expect(res.status).to.equal(201);
      expect(res.body).to.include({ id: 1, name: 'Alice' });
    });

    it('should get a user by id with GET /users/:id', async () => {
      await request(app).post('/users').send({ name: 'Bob' });
      const res = await request(app).get('/users/1');
      expect(res.status).to.equal(200);
      expect(res.body).to.include({ id: 1, name: 'Bob' });
    });

    it('should return 404 for GET /users/:id not found', async () => {
      const res = await request(app).get('/users/999');
      expect(res.status).to.equal(404);
      expect(res.body).to.have.property('error', 'User not found');
    });
  });

  describe('Task endpoints', () => {
    it('should return empty array for GET /tasks', async () => {
      const res = await request(app).get('/tasks');
      expect(res.status).to.equal(200);
      expect(res.body).to.be.an('array').that.is.empty;
    });

    it('should return 400 when creating task with non-existent user', async () => {
      const res = await request(app)
        .post('/tasks')
        .send({ title: 'Task1', assignedTo: 999 });
      expect(res.status).to.equal(400);
      expect(res.body).to.have.property('error', 'Assigned user does not exist');
    });

    it('should create a task with POST /tasks', async () => {
      await request(app).post('/users').send({ name: 'Carol' });
      const res = await request(app)
        .post('/tasks')
        .send({ title: 'Task1', assignedTo: 1 });
      expect(res.status).to.equal(201);
      expect(res.body).to.include({ id: 1, title: 'Task1', assignedTo: 1 });
      expect(res.body).to.have.property('createdAt');
    });
  });
});