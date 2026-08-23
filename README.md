# 🌱 Automatic-Plant-Watering(IoT & Embedded Control)
# Project Goal
> **An event-driven state machine built with Java (Firmata4j) and Arduino that waters plants based on real-time soil moisture and surrounding temperature.**

<img width="705" height="733" alt="statemachine" src="https://github.com/user-attachments/assets/77cf9da1-2d91-4130-9df2-37fe002306af" />
<br>

<img width="600" alt="Pump in water supply (2)" src="https://github.com/user-attachments/assets/320267d9-74b0-4ba6-86fd-a03a46599ca2" />




## Overview

This project solves a common problem of forgetting to water plants by automating the process. Unlike simple moisture triggers, it will check the temperature then adjust the amount of water provide to the plant. If the temperature is high (>25°C), the watering duration increases to prevent underwatering.
<br>
 - **Real-time Feedback**: OLED screen displays sensor voltage, temperature, soil status, and pump state.
     
   <img width="600"  alt="image" src="https://github.com/user-attachments/assets/53954d12-b42d-45c3-ace1-1ea7ad3ed64a" />
 - **Data Logging & Visualization**: Stores time-series data (Voltage vs. Time) in `ArrayLists` and plots live graphs using `StdDraw` on the PC.
     
    <img width="400" alt="image" src="https://github.com/user-attachments/assets/f6d3321d-47fa-4399-98b4-40f6fc9b95a2" />

 - **Fail-Safe Mechanism**: An emergency physical button instantly forces the system into a `STOP` state.

---
## 🛠️ Tech Stack
| Category | Technologies |
| :--- | :--- |
| **Embedded Hardware** | Arduino Uno, Grove Base Shield, Capacitive Soil Moisture Sensor, Temperature Sensor, MOSFET, Submersible Pump, OLED Display |
| **High-Level Logic** | Java 21 |
| **Communication** | Firmata4j (UART/Serial communication between Java and Arduino) |
| **Visualization** | StdDraw (Real-time voltage vs. time graph) |
| **Testing** | JUnit 5, System.out.print() debugging, Multimeter calibration |
<img width="600"  alt="testvoltage" src="https://github.com/user-attachments/assets/3f54c2ef-7c49-4276-bb62-0f82776a51cd" />
  
> **my friend helped me remove a wire a little away and took this picture**

---

## ⚙️ How It Works (State Machine Logic)

The system runs on a **periodic TimerTask** (every 60 seconds) and follows strict state transitions:

1. **Read Stage**: Reads analog values from the soil moisture sensor and temperature sensor.
2. **Calibration**: Converts raw sensor value (0-1023) to Voltage using the formula derived from multimeter testing:  
   **`V_out = 0.005 * (Sensor_Value) + 0.05`**
3. **Decision Logic**:
   - **If Soil = DRY**:
     - If Temp > 25°C → **Long Watering** (compensates for evaporation).
     - Else → **Short Watering**.
   - **If Soil = WET** → Pump remains **OFF**.
4. **Safety Cut-off**: Stops automatically when total water usage reaches the 2L limit (prevents flooding).
5. **Emergency Override**: Pressing the physical button triggers the `IODeviceEventListener`, immediately stop all operations (`STOP` state).

---

##  Calibration & Testing Data
To ensure accuracy, I measured the sensor output using a multimeter at different moisture levels and mapped them to Firmata4j readings:

| Soil State | Sensor Raw Value | Multimeter (V) |
| :--- | :--- | :--- |
| **Dry** | 735 | 3.71 V |
| **Moderately Wet** | 605 | 3.05 V |
| **Really Wet** | 527 | 2.65 V |

> Each value above represents the average of three measurements taken under each state.
> 
> **Regression Formula derived**: `y = 0.005x + 0.05` (where x = raw sensor value, y = voltage).

---

## 🐞 Major Bug Fix (Debugging Story)
**🐞 Problem 1**: During testing, the system continued turning on the pump even after the 2L water reservoir was used up.

**Investigation**: Used `print` statements to trace execution. Discovered that while the water count was updated inside the `PumpAction` class, the new value was **not being returned** to the main controller class.

**Solution**: Refactored the class to include a getter method that returns the updated water usage value to the main class, ensuring the system stops correctly when the limit is reached.
<br>
<br>
<br>
**🐞 Problem 2**: During testing, when the system got the mositure value and tried to plot it on the graph, only a single data point would appear. 

**Investigation**: I checked how the graph looked by letting the system run for different lengths of time. It turned out that the graph's domain was the issue. If the domain was set too large, it was hard to see any changes because the system reads data every 60 seconds, causing all the data to squish together.

**Solution**: Refactored the class, let a variable store the size of the graph's domain,  to let the graph's domain automatically adjust based on the amount of data collected.

---

## 📂 Project Structure (OOP Design)
- **`Mainrun`**: The **Application Entry Point**. Establishes the Firmata4j connection, verifies Arduino connectivity, initializes all components, and schedules the periodic execution of `KeyTask`.

- **`KeyTask`**: The **Central Controller (Orchestrator)** (extends `TimerTask`). Executed periodically, it organizes the main workflow: reads raw analog data, converts the data to voltage (via calibration formula), evaluates the soil state via `CheckSoilCondition`, and coordinates downstream actions including `PumpAction`, `OLEDdisplay`, and `GraphData`.

- **`CheckSoilCondition`**: Handles the core **Decision Logic**. Compares voltage value against defined thresholds, and determines the current soil state (Dry / Wet).

- **`PumpAction`**: Controls the relay switch for the water pump and tracks cumulative water usage. Stop watering automatically when the 2L safety limit is reached.

- **`OLEDdisplay`**: Manages the I2C communication with the OLED screen to display real-time feedback (voltage, temperature, soil status, and pump state) to the user.

- **`GraphData`**: Manages `ArrayLists` to store time-stamped voltage readings and renders the live trend chart on the PC using `StdDraw`.

- **`ButtonListener`**: Implements `IODeviceEventListener` to detect physical emergency button presses via hardware interrupts.

- **`Emergency`**: Encapsulates the Emergency Stop state. Once triggered by `ButtonListener`, it immediately signals the `KeyTask` controller to stop all operations and safely shut down the pump.

- **Event-Driven Architecture**: The system used `TimerTask` for periodic polling and `IODeviceEventListener` for asynchronous hardware interrupt handling, ensuring a responsive and reliable state-machine design.

---

## ❓ What I Learned
- **Embedded Integration**: Bridging Java (high-level GUI) with Arduino (low-level hardware) using Firmata4j.
- **OOP Principles**: Applied Inheritance (`TimerTask`), Abstraction (`IODeviceEventListener`), and Encapsulation (private variables with controlled access).
- **Hardware Calibration**: Using a multimeter to derive a linear regression equation for analog sensors.
- **Systematic Debugging**: Isolating state-variable synchronization issues using trace statements rather than guesswork.

---

## 🚩 How to Run (Quick Setup)
1. Connect Arduino board to your computer.
2. Flash the standard Firmata firmware to the Arduino Uno via Arduino IDE.
3. Connect sensors and pump according to the schematic (see wiring diagram in the first picture).
4. Place the pump inside water, place the moisture sensor inside the soil, place the temperature sensor near the plant.
5. Run the Java Main class (ensure Firmata4j and StdDraw libraries are in classpath).
6. Monitor the OLED screen or the PC console for real-time data.

---

## 💭 Future Improvements
- Break keyTask class into a few classes to match Single Responsibility Principle.
- Add WiFi/Bluetooth module to push data to a mobile app (IoT cloud).
- Implement a PID controller for precise moisture level maintenance.
- Use a database (SQLite) to store long-term historical trends.

---


**Feel free to reach out for any questions regarding the implementation!**

