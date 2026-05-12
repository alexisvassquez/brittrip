# 🚇 BritTrip

> *Mind the gap — between miles and kilometres, pounds and kilograms, Fahrenheit and Celsius.*

**BritTrip** is an Android app for anyone travelling between the United States and the United Kingdom. Whether you're a tourist trying to figure out how far that pub is, an expat weighing yourself at the GP, or a business traveller converting expenses on the fly — BritTrip gets you there without the mental arithmetic.

---

## What it converts

| Category | Conversions |
|----------|-------------|
| **Length** | Miles ↔ Kilometres, Feet ↔ Metres, Inches ↔ Centimetres, Yards ↔ Metres |
| **Weight** | Pounds ↔ Kilograms, Stone ↔ Kilograms, Ounces ↔ Grams |
| **Temperature** | °Fahrenheit ↔ °Celsius |
| **Volume** | Litres ↔ US Gallons, Litres ↔ Imperial Gallons, Litres ↔ US Pints, Litres ↔ Imperial Pints |
| **Currency** | USD ↔ GBP (live rate via [Frankfurter/ECB](https://www.frankfurter.app/)) |
| **Speed** | mph ↔ km/h |

---

## Features

- **Two-field converter** — tap, type, swap. No menus to dig through.
- **Quick picks** — one-tap access to the most common conversions per category.
- **Live currency rate** — fetched from the ECB via Frankfurter. Falls back gracefully if offline.
- **Travel Tips** — bite-sized cultural and practical notes for the US/UK traveller. Because a UK pint is bigger than a US pint, and you should know that before you order.
- **Conversion history** — review what you converted during your trip.

---

## Design

BritTrip takes visual cues from the London Underground — navy, red, and clean typography. The roundel lives in the header. The app should feel immediately at home to anyone who's ever waited on a Tube platform.

---

## Tech stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Build**: Gradle (AGP)
- **Minimum SDK**: API 26 (Android 8.0+)
- **Currency API**: [Frankfurter](https://www.frankfurter.app/) (free, ECB-sourced, no key required)

---

## Project structure

```
brittrip/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/brittrip/
│   │       │   ├── ui/          # Compose screens and components
│   │       │   ├── converter/   # Conversion logic
│   │       │   └── data/        # Currency API, history
│   │       └── res/             # Resources, drawables, strings
│   └── build.gradle.kts
├── legacy/
│   └── UnitConverter.java       # Original CLI prototype
├── .gitignore
└── README.md
```

---

## Origin

BritTrip started as a CLI Java unit converter — a small terminal app with exact conversion constants, a live currency fetch, and a clean menu system. The Android app is a ground-up rebuild of that same idea, bringing the same conversion accuracy to a proper mobile UI.

The CLI prototype lives in `/legacy` for reference.

---

## Status

🚧 Early development — Android project scaffolding in progress.

---

> For travellers, by someone who wants to move to Hackney Wick.

--

(c) Alexis M Vasquez, AMV Digital Studios - Software Engineer
