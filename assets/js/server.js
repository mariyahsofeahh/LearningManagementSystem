const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const app = express();
app.use(express.json());
app.use(cors()); // Allows your HTML files to talk to the backend

// 1. DATA LAYER (MongoDB Schema)
mongoose.connect('mongodb://localhost:27017/lms_db');

const Course = mongoose.model('Course', new mongoose.Schema({
    courseCode: String,
    title: String,
    description: String,
    lecturerId: String,
    students: [String]
}));

// 2. APPLICATION LAYER (Business Logic)
// Route to get all courses
app.get('/api/courses/all', async (req, res) => {
    const courses = await Course.find();
    res.json(courses);
});

// Route to create a course
app.post('/api/courses/create', async (req, res) => {
    const newCourse = new Course(req.body);
    await newCourse.save();
    res.status(201).json(newCourse);
});

// Route to get a single course detail
app.get('/api/courses/:id', async (req, res) => {
    const course = await Course.findById(req.params.id);
    res.json(course);
});

// Route to enroll a student
app.post('/api/courses/enroll', async (req, res) => {
    const { studentId, courseId } = req.body;
    await Course.findByIdAndUpdate(courseId, { $push: { students: studentId } });
    res.json({ message: "Enrolled!" });
});

app.listen(3000, () => console.log("LMS Backend running on port 3000"));