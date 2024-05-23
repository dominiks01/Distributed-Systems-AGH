import express from 'express';
import { Ice } from 'ice';
import { Detectors, Lights, Errors, Interfaces } from './SmartHome.js'; // Importing Detectors and Lights directly
import { promises as fsPromises } from 'fs';
import readline from 'readline';
import fetch from 'node-fetch';

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

const SERVER_URL = 'http://localhost:3000';
console.log("Welcome to the Smart Home CLI!");

const commandHandlers = {
    'getMode': (device) => `/get_mode?device=${device}`,
    'getSmokeDensity': (device) => `/smoke_density?device=${device}`,
    'isSave': (device) => `/is_safe?device=${device}`,
    'getLocation': (device) => `/get_location?device=${device}`,
    'setLocation': (device, params) => `/set_location?device=${device}&location=${params[0]}`,
    'changeColor': (device, params) => `/set_color?device=${device}&color=${params[0]}`,
    'getBrightnessLevel': (device) => `/get_brightness?device=${device}`,
    'getSensitivityLevel': (device) => `/get_sensitivity?device=${device}`,
    'setSensitivityLevel': (device, params) => `/set_sensitivity?device=${device}&sensitivity=${params[0]}`,
    'setBrightness': (device, params) => `/set_brightness?device=${device}&brightness=${params[0]}`,
    'changeMode': (device) => `/change_mode?device=${device}`,
    'turnOn': (device) => `/turn_on?device=${device}`,
    'turnOff': (device) => `/turn_off?device=${device}`,
    'isOn': (device) => `/is_on?device=${device}`,
    'activateAlarm': (device) => `/activate_alarm?device=${device}`,
    'deactivateAlarm': (device) => `/deactivate_alarm?device=${device}`,
    'motionDetected': (device) => `/motion_detected?device=${device}`,
    'getColor': (device) => `/get_color?device=${device}`,
    'notify': (device) => `/activate_alarm?device=${device}`,
    'getAlarm': (device) => `/get_alarm?device=${device}`,
    'setAlarm': (device, params) => `/set_alarm?device=${device}&start=${params[0]}&end=${params[1]}`
};

async function main() {

    rl.prompt();
    rl.on('line', async (input) => {
        const [device, command, ...params] = input.trim().split(' ');

        if(device == "listDevices"){
            await listDevices();
            return;
        }
        
        if(device == "listCommands"){
            await listCommands();
            return;
        }

        if(device == "quit"){
            rl.close();
            return;
        }

        const handler = commandHandlers[command];
        if (handler) {
            const endpoint = handler(device, params);
            await handleGetRequest(endpoint);
        } else {
            console.log("> Invalid command!");
            console.log("> Usage: [IDENTITY] [COMMAND] [PARAMS](Optional) ");
        }
        rl.prompt();
    });
}

async function handleGetRequest(endpoint) {
    try {
        const response = await fetch(`${SERVER_URL}${endpoint}`);

        if(response.status === 200){
            const data = await response.json();
            console.log(data);
        } else {
            const data = await response.text();
            console.log("Error response:", data, " - Status:", response.status);
        }
        
    } catch (error) {
        console.error("An error occurred:", error.message);
    }
}

const handleDeviceRequest = async (res, operation, device) => {
    try {
        const result = await operation();
        
        if (result === undefined) {
            // If operation returns void, send a custom message
            res.status(200).json(JSON.stringify(`Operation completed successfully for ${device}`));
        } else {
            res.status(200).json(result);
        }
    } catch (ex) {
        if (ex instanceof Errors.ActionNotPermitted) {
            res.status(403).send('Action not permitted');
            return; // Ensure function exits after sending response
        } else if (ex instanceof Ice.ObjectNotExistException) {
            res.status(400).send(`${device} : ObjectNotExistException '`);
            return; // Ensure function exits after sending response
        } else if (ex instanceof Errors.SensitivityLevelOutOfRange) {
            res.status(400).send("Brightness and Sensitivity should be in [1, 2, 3]");
            return; // Ensure function exits after sending response
        } else if (ex instanceof Errors.BrightnessLevelOutOfRange) {
            res.status(400).send("Brightness and Sensitivity should be in [1, 2, 3]");
            return; // Ensure function exits after sending response
        } else {
            console.log(ex)
            res.status(500).send('Something went wrong!');
            return; // Ensure function exits after sending response
        }
    } 
};
 
const app = express();
const port = 3000;

app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});

var ADDRES = "";

await readConfigFile().then(tcpParts => {
    if (tcpParts.length > 0) {
        ADDRES = ":tcp " + tcpParts[0]; 
        console.log(ADDRES);
    } else {
        ADDRES = ""; 
        console.log('No TCP parts found.');
    }
});

let communicator = Ice.initialize(process.argv);

app.get('/smoke_density', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const smoke_detector = await Detectors.SmokeDetectorPrx.checkedCast(base);
        return await smoke_detector.getSmokeDensity();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});


app.get('/is_safe', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const smoke_detector = await Detectors.SmokeDetectorPrx.checkedCast(base);
        return await smoke_detector.isSave();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});

app.get('/get_location', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const detector = await Interfaces.DetectorPrx.checkedCast(base);
        return await detector.getLocation();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});

app.get('/set_location', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const location = req.query.location
        const base = communicator.stringToProxy(device + ADDRES);
        const detector = await Interfaces.DetectorPrx.checkedCast(base);
        return await detector.setLocation(location);
    };

    await handleDeviceRequest(res, operation, req.query.device);
});

app.get('/set_color', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const color = req.query.color
        const base = communicator.stringToProxy(device + ADDRES);
        const detector = await Lights.GeneralLightsPrx.checkedCast(base);
        return await detector.changeColor(color);
    };

    await handleDeviceRequest(res, operation, req.query.device);
});

app.get('/get_brightness', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const detector = await Lights.OutdoorLightsPrx.checkedCast(base);
        return await detector.getBrightnessLevel();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});

app.get('/set_brightness', async (req, res) => {
    const operation = async () => {
        const brightness = req.query.brightness;
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const detector = await Lights.OutdoorLightsPrx.checkedCast(base);
        return await detector.adjustBrightness(brightness);
    };

    await handleDeviceRequest(res, operation, req.query.device);
});

app.get('/get_sensitivity', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const detector = await Interfaces.DetectorPrx.checkedCast(base);
        return await detector.getSensitivityLevel();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});

app.get('/set_sensitivity', async (req, res) => {
    const operation = async () => {
        const sensitivity = req.query.sensitivity;
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const detector = await Interfaces.DetectorPrx.checkedCast(base);
        return await detector.setSensitivityLevel(sensitivity);
    };

    await handleDeviceRequest(res, operation, req.query.device);
});

app.get('/change_mode', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const smart_device = await Interfaces.SmartDevicePrx.checkedCast(base);
        return await smart_device.changeMode();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});


app.get('/turn_on', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const light_control = await Interfaces.LightControlPrx.checkedCast(base);
        return await light_control.turnOn();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});

app.get('/turn_off', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const light_control = await Interfaces.LightControlPrx.checkedCast(base);
        return await light_control.turnOff();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});


app.get('/is_on', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const light_control = await Interfaces.LightControlPrx.checkedCast(base);
        return await light_control.isOn();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});

app.get('/get_mode', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const smart_device = await Interfaces.SmartDevicePrx.checkedCast(base);
        return await smart_device.getMode();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});


app.get('/activate_alarm', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const move_detector = await Detectors.MoveDetectorPrx.checkedCast(base);
        return await move_detector.activateAlarm();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});


app.get('/deactivate_alarm', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const move_detector = await Detectors.MoveDetectorPrx.checkedCast(base);
        return await move_detector.deactivateAlarm();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});

app.get('/motion_detected', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const move_detector = await Detectors.MoveDetectorPrx.checkedCast(base);
        return await move_detector.motionDetected();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});

app.get('/get_color', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const lights = await Lights.GeneralLightsPrx.checkedCast(base);
        return await lights.getColor();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});

app.get('/get_alarm', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const move_detector = await Detectors.MoveDetectorPrx.checkedCast(base);
        return await move_detector.getAlarm();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});

app.get('/set_alarm', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const start = req.query.start;
        const end = req.query.end;
        const move_detector = await Detectors.MoveDetectorPrx.checkedCast(base);

        const schedule = new Interfaces.DailySchedule;
        schedule.startHour = start;
        schedule.endHour = end;

        return await move_detector.setAlarm(schedule);
    };

    await handleDeviceRequest(res, operation, req.query.device);
});


app.get('/notify', async (req, res) => {
    const operation = async () => {
        const device = req.query.device;
        const base = communicator.stringToProxy(device + ADDRES);
        const smart_device = await Interfaces.SmartDevicePrx.checkedCast(base);
        return await smart_device.notify();
    };

    await handleDeviceRequest(res, operation, req.query.device);
});


async function readConfigFile() {
    try {
        const data = await fsPromises.readFile('config.client', 'utf8');
        const lines = data.split('\n');

        const tcpParts = lines.map(line => {
            const tcpMatch = line.match(/:tcp\s+(-h\s+\d+\.\d+\.\d+\.\d+\s+-p\s+\d+)/);
            return tcpMatch ? tcpMatch[1] : null;
        });

        return tcpParts.filter(part => part !== null);
    } catch (err) {
        console.error('Błąd podczas czytania pliku konfiguracyjnego:', err);
        return [];
    }
}

async function listDevices() {
    try {
        const data = await fsPromises.readFile('config.client', 'utf8');
        const lines = data.split('\n');

        const deviceNames = lines.map(line => {
            const nameMatch = line.match(/^([^=]+)\.Proxy/);
            return nameMatch ? nameMatch[1] : null;
        }).filter(part => part !== null);

        console.log(deviceNames);
    } catch (err) {
        console.error('Błąd podczas czytania pliku konfiguracyjnego:', err);
        return [];
    }
}

async function listCommands() {
    const commands = Object.keys(commandHandlers);
    console.log("List of commands:");
    commands.forEach(command => console.log(" > " + command));
}


app.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
});

main();
