Tak
===


### Introduction

This project is a CLI based tak game.
The rules for the game can be found in the file tak-rules.pdf.
It features a server for people to play online as well as an AI powered by a neural network.

The sub projects that make this up are:

 * [Tak Engine](https://github.com/joshatron/Tak-Engine)
 * [Tak CLI](https://github.com/joshatron/Tak-CLI)
 * [Tak AI](https://github.com/joshatron/Tak-AI)
 * [Tak Server](https://github.com/joshatron/Tak-Server)
 * [Tak Server Integration Tests](https://github.com/joshatron/Tak-Server-Integration-Tests)

### Running

To set up repo:

    git submodule init
    git submodule update

To install:

    ./install

A folder for the cli will be created, along with for the ai trainer.

To run the cli:

    ./tak

To run the trainer:

    ./ai-train

To run the server:

    ./game-server

To clean up the project:

    ./clean
