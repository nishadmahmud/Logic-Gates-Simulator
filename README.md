# Logic Gates Simulator
[![Apache-2.0 License](https://img.shields.io/badge/License-Apache--2.0-blue.svg)](https://choosealicense.com/licenses/apache-2.0/)

## Quick Description

A comprehensive Logic Gates Simulator made with the help of ___LWJGL___, ___Slick2D___ and ___JNA___ in ___Java 17___.

### Features

#### Basic Logic Gates
- **AND Gate**: Outputs TRUE when all inputs are TRUE
- **OR Gate**: Outputs TRUE when at least one input is TRUE  
- **NOT Gate**: Inverts the input signal
- **NAND Gate**: AND gate followed by NOT gate
- **NOR Gate**: OR gate followed by NOT gate
- **XOR Gate**: Outputs TRUE when inputs are different
- **XNOR Gate**: Outputs TRUE when inputs are the same
- **Buffer Gate**: Passes input signal unchanged

#### Arithmetic Components
- **Half Adder**: 2-bit addition with sum and carry outputs
- **Full Adder**: 3-bit addition with carry-in and carry-out
- **Half Subtractor**: 2-bit subtraction with difference and borrow
- **Full Subtractor**: 3-bit subtraction with borrow-in and borrow-out

#### Data Processing Components
- **4-to-2 Encoder**: Converts 4 input lines to 2 output lines
- **8-to-3 Encoder**: Converts 8 input lines to 3 output lines
- **2-to-4 Decoder**: Converts 2 input lines to 4 output lines
- **3-to-8 Decoder**: Converts 3 input lines to 8 output lines

#### Comparison Components
- **1-bit Comparator**: Compares two 1-bit values (A>B, A=B, A<B)
- **2-bit Comparator**: Compares two 2-bit values with full comparison outputs

#### Multiplexers and Demultiplexers
- **2-to-1 Multiplexer**: Selects between two inputs based on control signal
- **4-to-1 Multiplexer**: Selects between four inputs using two control signals
- **1-to-2 Demultiplexer**: Routes input to one of two outputs
- **1-to-4 Demultiplexer**: Routes input to one of four outputs

#### Sequential Logic Components
- **D Flip-Flop**: Data flip-flop with clock input
- **JK Flip-Flop**: Universal flip-flop with set, reset, and toggle capabilities
- **SR Flip-Flop**: Set-Reset flip-flop with clock input
- **T Flip-Flop**: Toggle flip-flop for frequency division
- **Clock Generator**: Provides periodic clock signals (0.5s interval)

#### Circuit Analysis Tools
- **Truth Table Generator**: Automatically generates complete truth tables for circuits
- **Boolean Equation Generator**: Generates and simplifies Boolean expressions for circuit outputs
- **Timing Diagram Viewer**: Real-time visualization of signal changes over time
- **Karnaugh Map Simplification**: Uses Quine-McCluskey algorithm for logic optimization

#### Circuit Management Features
- **Save/Load System**: Custom binary file format (.lgsworld extension) with version control
- **Clipboard Operations**: Multi-gate selection, copying, and pasting with preserved connections
- **Selection System**: Individual and multi-gate selection with Ctrl+click and box selection
- **Gate Settings**: Configurable gate names and input counts for variable-input gates
- **Cross-platform Compatibility**: Works on Windows, Linux, and macOS

#### User Interface Features
- **Visual Circuit Design**: Drag-and-drop interface for building circuits
- **Real-time Simulation**: Continuous circuit evaluation with immediate visual feedback
- **Connection Management**: Visual wire connections between gates
- **Zoom and Pan**: Navigate large circuits with mouse wheel zoom and drag pan
- **Keyboard Shortcuts**: Ctrl+C/Ctrl+V for copy/paste operations

### Planned Features

- Custom logic gates
- A logic calculator
- Circuit export to various formats
- Multi-bit operations support
- Advanced simulation features

## Running the program

If you don't have ___Java 17___ already installed, you'll need it now. Here's the [_link_](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).

After you have installed Java and added it to the ___PATH___, you can download the application jar from [_here_](https://github.com/nishadmahmud/Logic-Gates-Simulator/releases).

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

![Screenshot](https://aw4ozq14bt.ufs.sh/f/Vd9EBxWLpmoQrAkqki5tWgC4R6i3TOYPHLhynZobQK2JMNmw)
![screenshot](https://aw4ozq14bt.ufs.sh/f/Vd9EBxWLpmoQdaT5X3yURomcOh3QunIvXKbEa8VgPTkrNCMw)
![screenshot](https://aw4ozq14bt.ufs.sh/f/Vd9EBxWLpmoQ9jWB1jJUld5Q0NOF9IKom3SjaTLeJV4DwYEy)
![screenshot](https://aw4ozq14bt.ufs.sh/f/Vd9EBxWLpmoQKR2mFeSM6JZKjSaP2e7yzBElkf0xQFWwi8g5)
![screenshot](https://aw4ozq14bt.ufs.sh/f/Vd9EBxWLpmoQUZzBVn2dRlbDIc0BNA3fyTEkhHPvXgCOFnxi)
