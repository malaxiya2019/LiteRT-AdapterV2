# auto-step-samples（Android Architecture Samples）專案總覽

本 repo 是一個以「待辦事項（Todo）」為主題的 Android 架構範例，重點不在花俏功能，而在示範可測試、可維護、可擴充的分層設計。此分支採用 **Jetpack Compose + 單 Activity + Navigation Compose + ViewModel + Repository** 的經典現代 Android 組合。 

## 1. 專案定位與核心特色

- 以同一個 Todo domain 展示 Android app 架構實作。
- UI 層使用 Jetpack Compose，導航使用 Navigation Compose。
- 以 `ViewModel` 管理畫面狀態，搭配 Kotlin `Flow`/coroutines。
- Data layer 使用 Repository，整合本地 Room 與假遠端資料來源。
- 透過 Hilt 進行依賴注入。
- 測試涵蓋單元測試、整合測試與 Android instrumentation 測試。

## 2. 模組與目錄結構

### `:app`
主應用模組，包含：
- app 入口（`TodoActivity`、`TodoApplication`）
- 導航圖（`TodoNavGraph` / `TodoNavigation`）
- 功能畫面（tasks / taskdetail / addedittask / statistics）
- data layer（repository、local/network data source、model mapping）
- DI（Hilt modules）
- 單元測試與 Android 測試

### `:shared-test`
共用測試模組，提供可被 `:app` 的 unit test / androidTest 共用的測試基礎元件：
- Fake repository / fake dao / fake network source
- 自訂 test runner 與 coroutine rule
- 測試用 Hilt module

### 其他重要檔案
- `README.md`：專案定位與快速說明
- `settings.gradle.kts`：目前納入 `:app`、`:shared-test`
- `gradle/libs.versions.toml`：版本與依賴集中管理
- `screenshots/`：範例畫面資源

## 3. 執行流程（高層）

1. `TodoActivity` 啟動後設定 Compose 內容與主題。
2. 進入 `TodoNavGraph`，由 Navigation Compose 決定目前畫面。
3. 各畫面（Compose Screen）透過對應 `ViewModel` 取得狀態與處理使用者事件。
4. `ViewModel` 呼叫 `TaskRepository`（介面）進行資料讀寫。
5. `DefaultTaskRepository` 協調：
   - Local（Room `TaskDao`）
   - Network（fake `TaskNetworkDataSource`）
6. 資料透過 `Flow` 回流 UI，Compose 依 state 重新組合。

## 4. 分層設計重點

### UI / Presentation
- 每個 feature 通常是「Screen + ViewModel」配對。
- UI 盡量只處理顯示與事件轉發，狀態由 ViewModel 提供。
- 透過 Navigation Compose 做 route 與參數管理。

### Domain-ish（以 Repository 介面為中心）
- 專案沒有硬切獨立 domain module，但透過 `TaskRepository` 介面做抽象。
- 讓 ViewModel 不直接依賴 Room 或 Network 細節，降低耦合。

### Data
- `DefaultTaskRepository` 是資料協調核心。
- Local source：Room DB（`ToDoDatabase` + `TaskDao` + local entity）。
- Remote source：示範用途的 fake network data source。
- Mapping extension 將 local/network model 與 app model 互轉。

## 5. 依賴注入（Hilt）

- `RepositoryModule`：綁定 `TaskRepository` -> `DefaultTaskRepository`
- `DataSourceModule`：綁定 `NetworkDataSource` -> `TaskNetworkDataSource`
- `DatabaseModule`：提供 Room Database 與 `TaskDao`
- App 入口（Activity/Application）可透過 Hilt 自動注入依賴

## 6. 測試策略

- `app/src/test`：JVM unit tests（ViewModel、Repository、統計邏輯等）
- `app/src/androidTest`：instrumented tests（畫面互動、導航、DAO）
- `shared-test`：抽出可重用假資料源與測試基礎，避免重複樣板碼
- build 設定中啟用完整測試輸出，利於教學與除錯

## 7. 建置與技術棧摘要

- Kotlin + Gradle Kotlin DSL
- Java/Kotlin target 17
- Jetpack Compose（Material 3）
- Navigation Compose
- Room + KSP
- Hilt + KSP
- Coroutines / Flow
- Espresso / Compose UI Test / JUnit 等測試工具

## 8. 適合誰閱讀這個 repo

- 想學習「Compose 時代」Android 分層與測試的人
- 想看「可運行、可測試、架構清楚」範例的人
- 團隊想建立 app 架構 baseline、或做 code review 對照的人

---

如果你要更快掌握程式碼，建議閱讀順序：
1. `README.md`
2. `TodoActivity` / `TodoNavGraph`
3. 任一 feature（例如 `tasks`）的 `Screen + ViewModel`
4. `TaskRepository` 與 `DefaultTaskRepository`
5. `di/` 下的 Hilt modules
6. `app/src/test` 與 `app/src/androidTest` 代表性測試
