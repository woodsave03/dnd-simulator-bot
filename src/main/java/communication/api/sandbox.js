import fetch from 'node-fetch';
let apiEndpoint = "https://api.open5e.com/v1/?format=json";

export async function fetchData(apiURL) {
    try {
        if (apiURL === undefined || apiURL === '' || apiURL === null) {
            apiURL = apiEndpoint;
        }
        const response = await fetch(apiURL);
        const body = await response.json();
        console.log(JSON.stringify(body));
    } catch (error) {
        console.error(JSON.stringify({error: error.message}));
    }
}

const apiUrl = process.argv[2];
fetchData(apiUrl);
