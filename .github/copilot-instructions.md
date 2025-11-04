# Github Copilot Instructions - katastroplis

This file contains instructions for GitHub Copilot to help it understand the context and requirements of the project.

## Project Overview

`katastroplis` is a multi-module kotlin project. The naming part `kata` indicates that the code implemented here is not intended for
production use, but rather for educational purposes, such as practicing coding skills or learning new concepts.

## General Guidelines

When instructions use the word `must`, it indicates a requirement that should be strictly followed. If the word `should` is used, it indicates a recommendation that is generally advisable but not strictly required.

- The project communication language is (american) english. All code, docs and the chat with the copilot agent must be done in english.
- The sole programming language used in this project is Kotlin.
- We use gradle as the build tool.
  - The build is done with the `./gradlew` wrapper. The version of this wrapper is defined in the [gradle/wrapper/gradle-wrapper.properties](../gradle/wrapper/gradle-wrapper.properties) file.
    Always conform to the coding styles are defined in the files listed in Coding Conventions and Architecture Decision Records when generating code.
- Use @terminal when answering questions about Git. 
- Answer all questions in the style of a friendly colleague, using informal language.
- Answer all questions in less than 1000 characters, and words of no more than 12 characters.
- 
## Coding Conventions

The coding conventions are defined in the [docs/CodingConventions.md](../docs/CodingConventions.md) file. You must follow these conventions when writing code.

## Architecture Decision Records

Core architecture decisions are documented in the [docs/ArchitectureDecisionRecords.md](../docs/ArchitectureDecisionRecords.md) file. You must follow these decisions when writing code.
