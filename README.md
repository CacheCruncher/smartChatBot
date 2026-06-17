# SmartChatBot

A fully offline Android chatbot app built with Jetpack Compose, Room, and MVVM/MVI architecture.

## Screenshots
## Screenshots

<p align="center">
  <img src="./screenshot/after_feedback_retun_different_result.png" width="45%" alt="SmartChatBot Chat screen after feedback" />
  <img src="./screenshot/search_result_hide_chat_messages.png" width="45%" alt="SmartChatBot search Screen" />
</p>

## How to run

1. Clone the repo
2. Open in Android Studio
3. Run on any device or emulator with API 26+

## Matching approach

I went with keyword matching. The user's query is split into words, and each stored question is checked for how many of those words it contains. The entry with the highest match also factors in a weight value - every helpfull adds 0.1 to the weight, every not helpful subtracts 0.1. So the final answers has high rank.

I chose this because it is simple to implement. For a 50-entry offline dataset, accuracy is good enough and the logic fits in a few lines.

## What I'd improve with more time

- Add TF-IDF or fuzzy matching as an alternative strategy
- Better fallback when no mach is available.
- More unit tests covering edge cases.
