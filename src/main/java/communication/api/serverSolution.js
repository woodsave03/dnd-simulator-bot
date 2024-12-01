import fetch from 'node-fetch';
import express from 'express';

const app = express();

app.use(express.json());

const PORT = 3000;
const ADDRESS = 'http://localhost:' + PORT + '/fetch';
let apiEndpoint = "https://api.open5e.com/v1/?format=json";


function isValidString(param) {
    return param !== null && param !== undefined && param.length !== 0;
}

function isValidInteger(param) {
    return param !== null && param !== undefined && parseInt(param) !== undefined;
}

const API_CLASS_MAP = {
    "barbarian": 0,
    "bard": 1,
    "cleric": 2,
    "druid": 3,
    "fighter": 4,
    "monk": 5,
    "paladin": 6,
    "ranger": 7,
    "rogue": 8,
    "sorcerer": 9,
    "warlock": 10,
    "wizard": 11,
    default: -1
};

const API_SPELLCASTERS = {
    "bard": 0,
    "cleric": 1,
    "druid": 2,
    "paladin": 3,
    "ranger": 4,
    "sorcerer": 5,
    "warlock": 6,
    "wizard": 7,
    default: -1
};

app.get('/fetch', async (req, res) => {
    try {
        const response = await fetch(apiEndpoint);
        const data = await response.json();
        res.json({
            status: 'success',
            statusText: '200',
            data: data
        });
    } catch (error) {
        res.json({
            status: 'error',
            statusText: '500',
            message: 'Failed to fetch API data'
        });
    }
});

app.get('/class/:className', async (req, res) => {
    const className = req.params.className;

    if (!isValidString(className)) {
        return res.json({
            status: 'error',
            statusText: '300',
            message: 'Class name not provided.'
        });
    } else if (API_CLASS_MAP[className.toLowerCase()] === -1) {
        return res.json({
            status: 'error',
            statusText: '300',
            message: 'Invalid class name.'
        });
    }

    try {
        const response = await fetch(ADDRESS);
        const results = await response.json();

        // Log the entire results object for inspection
        // console.log(results);

        // Assuming the 'classes' array is nested within a 'data' property
        const classesUrl = results.data.classes;

        const classesResponse = await fetch(classesUrl);
        const classesData = await classesResponse.json();

        res.json({
            status: 'success',
            statusText: '200',
            data: classesData.results[API_CLASS_MAP[className.toLowerCase()]]
        });
    } catch (error) {
        res.json({
            status: 'error',
            statusText: '500',
            message: 'Failed to fetch class data.',
            error: error.message
        });
    }
});

app.get('/class/:className/spells/', async (req, res) => {
    const className = req.params.className;

    if (!isValidString(className)) {
        return res.json({
            status: "error",
            statusText: '300',
            message: 'Class name not provided.'
        });
    } else if (API_SPELLCASTERS[className.toLowerCase()] === -1) {
        return res.json({
            status: 'error',
            statusText: '300',
            message: 'Class does not have a spell list.'
        });
    }

    try {
        const APIResponse = await fetch(ADDRESS);
        const APILinks = await APIResponse.json();

        const spellListURL = APILinks.data.spelllist;

        const SLResponse = await fetch(spellListURL);
        const results = await SLResponse.json();

        const SLData = results.results[API_SPELLCASTERS[className.toLowerCase()]];

        res.json({
            status: 'success',
            statusText: '200',
            data: SLData
        });
    } catch (error) {
        res.json({
            status: "error",
            statusText: '500',
            message: 'Error fetching class spell list data.'
        });
    }
});

app.get('/spells', async (req, res) => {
    const page = req.query.page;

    if (!isValidInteger(page)) {
        return res.json({
            status: 'error',
            statusText: '300',
            message: 'Page number is a required integer.'
        });
    }

    const pageNum = parseInt(page);

    try {
        const APIResponse = await fetch(ADDRESS);
        const APILinks = await APIResponse.json();

        const spellURL = APILinks.data.spells;

        const spellResponse = await fetch(spellURL + `&page=${pageNum}`);
        const spellJSON = await spellResponse.json();

        const spells = spellJSON.results;

        res.json({
            status: 'success',
            next: spellJSON.next,
            data: spells
        });
    } catch (error) {
        res.json({
            status: 'error',
            statusText: '500',
            message: 'Failed to fetch spells data.'
        });
    }
});

app.get('/feats', async (req, res) => {
    const page = req.query.page;
    if (!isValidInteger(page)) {
        return res.json({
            status: 'error',
            statusText: '300',
            message: 'Page number is a required integer.'
        });
    }

    const pageNum = parseInt(page);
    try {
        const APIResponse = await fetch(ADDRESS);
        const APILinks = await APIResponse.json();
        const featsLink = APILinks.data.feats;

        const featsResponse = await fetch(featsLink + `&page=${pageNum}`);
        const featsJSON = await featsResponse.json();
        const feats = featsJSON.results;

        res.json({
            status: 'success',
            statusText: '200',
            next: featsJSON.next,
            data: feats
        });
    } catch (error) {
        res.json({
            status: 'error',
            statusText: '500',
            message: 'Error retrieving feat data.'
        });
    }
})

app.get('/conditions', async (req, res) => {
    try {
        const APIResponse = await fetch(ADDRESS);
        const APILinks = await APIResponse.json();
        const conditionsURL = APILinks.data.conditions;

        const conditionsResponse = await fetch(conditionsURL);
        const conditions = await conditionsResponse.json();
        const data = conditions.results;

        res.json({
            status: 'success',
            statusText: '200',
            data: data
        });
    } catch (error) {
        res.json({
            status: 'error',
            statusText: '500',
            message: 'Error retrieving conditions data.'
        });
    }
});

app.get('/races', async (req, res) => {
    const page = req.query.page;
    if (!isValidInteger(page)) {
        return res.json({
            status: 'error',
            statusText: '300',
            message: 'Page number is a required integer.'
        });
    }

    const pageNum = parseInt(page);
    try {
        const APIResponse = await fetch(ADDRESS);
        const APILinks = await APIResponse.json();
        const racesURL = APILinks.data.races;

        const raceResponse = await fetch(racesURL);
        const races = await raceResponse.json();
        const data = races.results;
        const next = races.next;

        res.json({
            status: 'success',
            statusText: '200',
            next: next,
            data: data
        });
    } catch (error) {
        res.json({
            status: 'error',
            statusText: '500',
            message: 'Error retrieving race data.'
        });
    }
});

app.get('/weapons', async (req, res) => {
    try {
        const APIResponse = await fetch(ADDRESS);
        const APILinks = await APIResponse.json();
        const weaponsURL = APILinks.data.weapons;

        const weaponsResponse = await fetch(weaponsURL);
        const weapons = await weaponsResponse.json();
        const data = weapons.results;

        res.json({
            status: 'success',
            statusText: '200',
            data: weapons
        });
    } catch (error) {
        res.json({
            status: 'error',
            statusText: '500',
            message: 'Error retrieving weapons data.'
        });
    }
});

app.get('/armor', async (req, res) => {
    try {
        const APIResponse = await fetch(ADDRESS);
        const APILinks = await APIResponse.json();
        const armorURL = APILinks.data.armor;

        const armorResponse = await fetch(armorURL);
        const armor = await armorResponse.json();
        const data = armor.results;

        res.json({
            status: 'success',
            statusText: '200',
            data: data
        });
    } catch (error) {
        res.json({
            status: 'error',
            statusText: '500',
            message: 'Error retrieving armor data.'
        });
    }
});

app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});