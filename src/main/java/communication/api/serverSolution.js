import fetch from 'node-fetch';
import express from 'express';
const app = express();
app.use(express.json());
const PORT = 3000;
let apiEndpoint = "https://api.open5e.com/v1/?format=json";

app.get('/fetch', async (req, res) => {
    try {
        const response = await fetch(apiEndpoint);
        const data = await response.json();
        res.json(data)
    } catch (error) {
        res.json({
            status: 500,
            error: 'Failed to fetch data'
        });
    }
});

app.get('/:class', async (req, res) => {
    try {
        const classesUrl = await fetch(apiEndpoint).json().classes;
        const data = await fetch(classUrl);
    } catch (error) {

    }
})

app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});