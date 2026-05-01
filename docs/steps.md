# 底盤程式教學

## 程式架構介紹

在FRC程式中，主要的程式架構有兩種，一種是計時(Timed Based)模式，另外一種是指令模式(Command Based)，這兩種程式的方式各有好壞，可以根據自己的需要來做撰寫

| 項目 | Timed Baed | Command Based |
|-----| -----------|---------------|
|程式長度 | 短 | 長|
|程式複雜度 | 低 | 高
|大程式除錯難易度 | 高 | 低 |
|編寫時間| 短 | 長|
|Debug爆氣難易度| 高 | 低 |

Timed Based主要會用在需要快速開發(例如speedrun KOP底盤)或者是前期測試(例如賽季剛開始在試API)的時候會用到，換到機器的程式的時候(例如區域賽用的機器)就會比較適合用Command Based, 因為他把一個機器拆成很多字系統(subsystem)並且可以拆分檔案，這樣不管是在Debug還是進度追蹤都會比Timed Based簡單

## Command Based 簡介

Command Based主要有兩個大東西，分別是子系統(subsystem)與指令，一個子系統通常代表一個機構(例如intake或shooter或climber)，一個指令就代表一個動作(例如吃球或者是射球)

直得注意的是，一個子系統一次只能跑一個指令(就像是一個人不能同時往左走跟往右走)，目的就是為了要保護機構跟防止人為的智障損失。

### 一個簡單的小例子🌰

今天有一個360的旋轉炮台，它有三個馬達，分別是`FacingMotor`跟`PitchingMotor`跟`ShootingMotor`， 然後這個子系統會有`pitchAngle(Angle)`, `turnTo(Rotation2d)`跟`shoot(LinearVelocity)`三個指令，分別是

```java
public Command pitchingAngle(Angle target){
    return run(() -> PitchingMotor.setControl(PitchingPID.withPosition(target)));
}

public Command turnTo(Rotation2d target){
    return run(() -> FaacingMotor.setControl(TurningPID.withPosition(target.getMeasure())));
}

public Command shoot(LinearVelocity vel){
    return run(() -> ShootingMotor.setControl(ShootPID.withVelocity(RadiansPerSecond.of(vel.in(MetersPerSecond)/WheelRadius.in(meters)))));
}
```
#### 小補充 
這邊會把它變成是Supplier是因為run函數他吃的是Runnable, 然後可以放supplier, 關於supplier的資料可以看 [這邊](https://ithelp.ithome.com.tw/m/articles/10319327)

#### 拉回來

這個時候如果把射擊的指令寫成`shoot(V).anlongWith(pitchingAngle(A).alongWith(turnTo(T)))`，那他就會在呼叫的時候跟你講說這三個指令依賴同一個子系統就不讓你跑(關於為什麼這樣寫會依賴同一個子系統一下會說)。這時候要解的方法有像是把這三個整理成一個指令`shoot(Angle A, Rotation2d T, velocity T)` 或是把其中兩個的requirements拔掉，這樣就不會報錯了(不過在現實情況中應該會是`turnTo(Rotation2d)`跟`pitchingAngle(Angle)`會被整理成一個指令叫`prepare()`直接設default command(資料就從API那邊抓),然後再把shoot的requirements拔掉這樣這三個東西才會一起運作)

## 馬達API

- REVLib
    - SparkMax
        - 所有無刷馬達
        - 有刷馬達(如果要閉環控制的話要外接Encoder)
    - SparkFlex
        - NEO Vortex
        - NEO v2.0
- Phoenix 6
    - TalonFX
        - Kraken X60, X44
    - TalonFXS
        - 所有無刷馬達

- Phoenix 5
    - TalonSRX 可外接encoder
    - VictorSPX 不可外接encoder
        - 有刷馬達