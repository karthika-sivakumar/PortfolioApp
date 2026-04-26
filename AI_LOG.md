## AI_LOG.md

## Tools used
- ChatGPT  
- Gemini  
- Claude  

---

## Significant prompts

### Prompt 1 —  Project Structure
> I'm building a Portfolio Holdings app with two screens (Holdings list + Holding detail), pull-to-refresh, explicit UI states, and local cached data. Generate folder structure for data, domain, presentation, and di layers.

**What AI produced:**  
- Clean layered folder structure  
- Separation into data, domain, presentation  

**What I kept / changed:**  
- Used structure mostly as-is  
- Added a **use case layer** to better separate business logic from ViewModel

---

### Prompt 2 — Domain Layer
> Build the domain layer in pure Kotlin only, zero Android or third-party dependencies. Include Holding and Transaction models with computed properties like profitLoss and totalValue, and the PortfolioRepository interface.

**What AI produced:**  
Holding, Transaction models with P&L calculations

**What I kept / changed:**  
- Used models mostly as-is  
- Refined computed properties and validations  

---

### Prompt 3 — Data Layer
> Build the data layer — Room entities, DAOs, JsonDataSource reading from assets, mappers from DTO to Entity to Domain, and PortfolioRepositoryImpl. Room is the single source of truth, JSON only writes into it on refresh.

**What AI produced:**  
Entities, DAO interfaces, repository implementation, and JSON parsing.

**What I kept / changed:**  
- Kept the overall flow
- Fixed some mapping inconsistencies (types and enum conversions)
- Ensured proper error propagation instead of silent failures 

---

### Prompt 4 — Presentation Layer
> Build Compose UI with two screens, pull-to-refresh, and explicit loading/empty/error states. Use MVVM with StateFlow. No business logic in composables.

**What AI produced:**  
- Compose screens with UiState handling  
- Navigation setup  

**What I kept / changed:**  
- Kept the structure.
- Fixed some layout issues and removed a silent try-catch that was hiding errors from the UI.

---

### Prompt 5 — Tests
> Generate unit tests for domain, data, and presentation layers using JVM tests.

**What AI produced:**  
Tests using mocks (Mockito-style)

**What I kept / changed:**  
- Replaced mocks with **fake implementations**  
- Fixed coroutine timing and test stability issues  

---

## A bug your AI introduced

**Issue:**  
Performance bar logic did not handle negative values correctly, resulting in no visible bar for losses.

**How I caught it:**  
Observed incorrect UI behavior during testing.

**Fix:**  
Used absolute value for normalization and relied on color to indicate gain/loss.

---

## A design choice you made against AI suggestion

AI leaned toward directly using repository logic inside ViewModel.

**What I did instead:**  
Introduced a **use case layer**.

**Why:**  
- Better separation of concerns  
- Cleaner architecture  
- Easier testing and scalability  

---

## Time split
- Writing code: ~15%  
- Prompting AI: ~40%  
- Reviewing AI output: ~20%  
- Debugging & testing: ~25%
