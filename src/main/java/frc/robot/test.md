如何寫一個好的 PR
===============

在這篇文章中會告訴你一個好的PR(Pull Request)會需要哪些條件

## 背景知識

PR(Pull Request)是在大型專案中一個控制版本的途徑，在多人協作的程式中，因為每個人都會有自己獨立一份的copy，在自己的copy中沒有人會知道你改了什麼，於是就會需要跟大家講說你改了什麼有什麼功能這樣。

## 一個好的PR需要什麼

### 需要先有共識的東西

1. 
    函數/變數的命名邏輯：在程式中，每個人命名函數和變數的習慣都不一樣，那再多人協作的時候就會是一場災難，於是，要在一開始的時候就要說好函數是怎麼命名的

    表一：命名的流派

    | 種類 | 特徵 |
    | ----|------|
    | 底線派 | this_is_Variable |
    | 首字大寫派 | thisIsVariable |
    | 真・首字大寫派 | ThisIsVariable |

    編者的命名習慣是: 對於變數會用全部的首字大寫(例如`IntakeMotor`)，函數的就會只有第一個字小寫其他的都大寫(例如`resetRobotPosition`)

2.
    統一註解的語言：在所有人的母語都一樣的情況下，建議都統一程式裡面註解的語言，因為有些人(我絕對不會承認)會因為懶得切換輸入法所以就連註解跟PR都是全英文的，這在code review的時候會增加一些的溝通難度跟翻譯斷層(不過寫code很常看英文的東西英文應該不會太差吧(嘴硬))

### 平常就要培養的習慣

1. 寫註解

    在多人共編的程式中，因為別人不知道你在想什麼，所以註解就成了一個非常重要的東西，所謂的註解不是像

    ```java
    public SparkMax motor; //decleare a new motor object
    ```

    這種幹話，而是像是

    ```java

    public Command drive(Pose2d pose){
        try{
                return new PathfindingCommand(
                pose, 
                Constants.AutoConstraints, 
                this::getPose, 
                this::getSpeeds,
                (speeds, ff) -> drive(speeds), 
                new PPHolonomicDriveController(
                    new PIDConstants(0,0), 
                    new PIDConstants(0,0)), //TODO: the value need fine tuning.
                RobotConfig.fromGUISettings(), 
                this);
            }catch(Exception e){
                DriverStation.reportWarning(e.getMessage(), e.getStackTrace());
                return idle();
            }
    }

    ```

    寫著借咬寫有意義的東西才會有人想看，而且也可以利用TODO, FIXME等等的字，因為在多數人裡面都會有裝(even) better comments, 裡面就會自帶這些程式碼的高亮，那這也可以讓其他開發者知道這裡需要多看幾眼。

2. 定義有意義的變數與避免魔法數字

    類似剛剛的註解，變數名稱要定義的讓人看得懂，還有要避免magic number的出現(就是一串資料長得很奇怪但不知道爲什麼會長這樣)。

    一個不好的例子就像是

    ```java
    public SparkMax m_motor;
    ```

    在沒有其他背景資料如檔案名稱，資料類別的幫助下，沒有人知道你這個是要衝尛用的，所以為了方便其他人容易知道你在幹嘛，可以改成

    ```java
    public SparkMax LeftDriveMotor;
    ```

    這樣可以讓別人更快速的抓到這個東西是在幹嘛的，對於code review的人來說，工作量也比較小。

    第二個就是麼法數字，一些數字像是
    ```java
    public Pose2d StartingPose = new Pose2d(11,4.1,5, Rotation2d.fromDegrees(14));
    ```

    在沒有額外註解的情況下，別人很難知道為什麼數字會長這個洨樣，所以可以是時候添加註解

    ```java
    public Pose2d StartingPose = new Pose2d(11,4.1,5, Rotation2d.fromDegrees(14)); //robot offset applied
    ```

    可以讓別人(或是三天後的自己)更容易知道這個為什麼會長這樣

## PR實作

1. 找到Pull Request的按鈕

    以GitHub來說會在最上面的導覽列就會出現了，找到之後就用力的按下去

2. 按下 Create New Request


    因為GitHub是把issue(那又是另外一個東西了)跟PR是丟在一起管理的，所以要注意是在PR頁這邊建立

3. 選擇比較對象
    在這個時候就要選擇是要跟哪兩個分支比，通常是自己的Fork跟主專案來比，不過這邊是測試用所以就長這個樣子

    他會把所有在這之間所有的commit都列出來，然後讓你先看一次你的code有沒有改錯。

    (這時候也會意識到commit message的重要性，不要隨便亂打啊啊啊)

4. 寫PR時間


    PR的title就會是他會顯示在最外面的名字，裡面的description就是要寫你在幹嘛

    一個通用的PR格式是

    [Overview]

    [Fixed Bugs]

    [New Features]

    [Questions]

    [other appendix]



然後就等Code reviewer看你的扣了，那在這時候會有分三種情況

- Approved: 通過
- Comment: 大致上沒問題不過有一些小地方需要改
- Request Changes: 沒錯就是要趕掉重練

(小提醒：在pr關閉之前所有的commit都會被列進PR裡面，所以被review過好的code就不要再改ㄌ)

大概是這樣，如果有什麼缺漏之後會再補
以上，波掰