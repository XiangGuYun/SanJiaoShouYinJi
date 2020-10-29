import java.io.File

println("请输入Activity的名称(无需加上‘Activity’)")
val activityName = readLine()

val file1 = File("${activityName}Activity.kt")
file1.createNewFile()
file1.writeText("""
    import android.os.Bundle
    import com.yp.baselib.annotation.LayoutId
    import com.yp.baselib.base.BaseActivity

    @LayoutId(R.layout.activity_main)
    class TestActivity : BaseActivity() {
        
        override fun init(bundle: Bundle?) {
            
        }
        
    }
""".trimIndent())

val file2 = File("activity_${activityName?.toLowerCase()}.xml")
file2.createNewFile()
file2.writeText("""
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
</FrameLayout>
""".trimIndent())

