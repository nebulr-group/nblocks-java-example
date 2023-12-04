# Nblocks React example project

This backend project contains a simple demonstration app to showcase how easily Nblocks can be integrated into your existing stack.

We use this project in some of our tutorials and posts to show some of the features of Nblocks.

Read more about Nblocks [here](https://nblocks.dev)

## Credits
This Java project was forked from this [Github Repo](https://github.com/microsoft/vscode-remote-try-java).

## Setup

This project was uses Maven

### Compiling
```
mvn compile
```

### Run the App class
```
mvn exec:java -Dexec.mainClass="com.mycompany.app.App"
```

## Setup in VS Code Dev Containers

Follow these steps to open this sample in a container using the VS Code Dev Containers extension:

1. If this is your first time using a development container, please ensure your system meets the pre-reqs (i.e. have Docker installed) in the [getting started steps](https://aka.ms/vscode-remote/containers/getting-started).

2. To use this repository, open a locally cloned copy of the code:
   - Clone this repository to your local filesystem.
   - Press <kbd>F1</kbd> and select the **Dev Containers: Open Folder in Container...** command.
   - Select the cloned copy of this folder, wait for the container to start
   - Press <kbd>F5</kbd> to launch the app with the debugger in the container