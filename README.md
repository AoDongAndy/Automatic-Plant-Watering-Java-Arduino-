# 🌱 Automatic-Plant-Watering(IoT & Embedded Control)
# Project Goal
> **An event-driven state machine built with Java (Firmata4j) and Arduino that waters plants based on real-time soil moisture and surrounding temperature.**
<div align="center">
<img width="600" alt="Pump in water supply (2)" src="https://github.com/user-attachments/assets/320267d9-74b0-4ba6-86fd-a03a46599ca2" />
</div>

##Overview
<br>
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

The system runs on a **periodic TimerTask** (every 10 seconds) and follows strict state transitions:

1. **Read Stage**: Reads analog values from the soil moisture sensor and temperature sensor.
2. **Calibration**: Converts raw ADC (0-1023) to Voltage using the formula derived from multimeter testing:  
   **`V_out = 0.005 * (Sensor_Value) + 0.05`**
3. **Decision Logic**:
   - **If Soil = DRY**:
     - If Temp > 25°C → **Long Watering** (compensates for evaporation).
     - Else → **Short Watering**.
   - **If Soil = WET** → Pump remains **OFF**.
4. **Safety Cut-off**: Stops automatically when total water usage reaches the 2L limit (prevents flooding).
5. **Emergency Override**: Pressing the physical button triggers the `IODeviceEventListener`, immediately halting all operations (`STOP` state).

---

##  Calibration & Testing Data
To ensure accuracy, I measured the sensor output using a multimeter at different moisture levels and mapped them to Firmata4j readings:

| Soil State | Sensor Raw Value | Multimeter (V) |
| :--- | :--- | :--- |
| **Dry** | 735 | 3.71 V |
| **Moderately Wet** | 605 | 3.05 V |
| **Really Wet** | 527 | 2.65 V |

> **Regression Formula derived**: `y = 0.005x + 0.05` (where x = raw sensor value, y = voltage).

---

## 🐞 Major Bug Fix (Debugging Story)
**Problem**: During testing, the system continued watering even after the 2L water reservoir was depleted.

**Investigation**: Used `print` statements to trace execution. Discovered that while the water count was updated inside the `PumpAction` class, the new value was **not being returned** to the main controller class.

**Solution**: Refactored the class to include a getter method that returns the updated water usage value to the main loop, ensuring the system halts correctly when the limit is reached.

---

## 📂 Project Structure (OOP Design)
- **`CheckSoilCondition`**: Handles sensor calibration and state determination.
- **`PumpAction`**: Controls the relay logic and tracks water usage limits.
- **`GraphData`**: Manages `ArrayLists` to store time/voltage pairs and renders the live graph.
- **Event-Driven**: Implements `IODeviceEventListener` for the emergency button and extends `TimerTask` for periodic sampling.

---

## ❓ What I Learned
- **Embedded Integration**: Bridging Java (high-level GUI) with Arduino (low-level hardware) using Firmata4j.
- **OOP Principles**: Applied Inheritance (`TimerTask`), Abstraction (`IODeviceEventListener`), and Encapsulation (private variables with controlled access).
- **Hardware Calibration**: Using a multimeter to derive a linear regression equation for analog sensors.
- **Systematic Debugging**: Isolating state-variable synchronization issues using trace statements rather than guesswork.

---

## 🚩 How to Run (Quick Setup)
1. Flash the standard Firmata firmware to the Arduino Uno via Arduino IDE.
2. Connect sensors and pump according to the schematic (see wiring diagram in repo).
3. Run the Java Main class (ensure Firmata4j and StdDraw libraries are in classpath).
4. Monitor the OLED screen or the PC console for real-time data.

---

## 💭 Future Improvements
- Add WiFi/Bluetooth module to push data to a mobile app (IoT cloud).
- Implement a PID controller for precise moisture level maintenance.
- Use a database (SQLite) to store long-term historical trends.

---


**Feel free to reach out for any questions regarding the implementation!**

