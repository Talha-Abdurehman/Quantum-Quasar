// server.js
const express = require('express');
const http = require('http');
const {Server} = require('socket.io')

const app = express();
const server = http.createServer(app);
const io = new Server(server);

// Serve a simple message for the root endpoint
app.get('/', (req, res) => {
    res.send('Socket.IO server is running');
});

let players = {}

// When a player (client) connects
io.on('connection', (socket) => {
    console.log('A player connected with socket ID:', socket.id);


    socket.on('playerJoin', (data) => {
        players[socket.id] = data;
        console.log(`Player: ${socket.id.trim()}`, data);
        socket.broadcast.emit('newPlayer', {id: socket.id, ...data})
    });

    // Handle incoming messages from the client
    socket.on('playerUpdate', (data) => {
        console.log(`Player: ${socket.id.trim()}`, data);
        socket.broadcast.emit("playerUpdate", {id: socket.id, ...data})
    });

    // Handle disconnect event
    socket.on('disconnect', () => {
        console.log('A player disconnected');
    });

    socket.on('error', (error) => {
        console.error('Socket error:', error);
    });
});

const PORT = 3000;
server.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});
