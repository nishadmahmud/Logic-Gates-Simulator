
# Logic Gates Simulator
[![Apache-2.0 License](https://img.shields.io/badge/License-Apache--2.0-blue.svg)](https://choosealicense.com/licenses/apache-2.0/)
## Quick Description

A Logic Gates Simulator made with the help of ___LWJGL___, ___Slick2D___ and ___JNA___ in ___Java 17___.

### Features

- All basic logic gates
- A save/load system
- Cross platform

### Planned Features

- Custom logic gates
- A logic calculator
## Running the program

If you don't have ___Java 17___ already installed, you'll need it now. Here's the [_link_](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).

After you have installed Java and added it to the ___PATH___, you can download the application jar from [_here_](https://github.com/muscaa/logic-gates-sim/releases).

Now you place it where you want and create in that dir:

- on ___windows___:

  a ___run.bat___ file containing:

  ```bash
  start javaw -jar logic-gates-sim.jar
  ```

- on ___linux/macos___:

  a ___run.sh___ file containing:

  ```bash
  javaw -jar logic-gates-sim.jar
  ```

## Building from source

Clone this repo on your machine and build it with:

```bash
gradlew build
```
    
## Screenshots

![Screenshot](https://github.com/muscaa/logic-gates-sim/blob/main/images/screenshot.png?raw=true)