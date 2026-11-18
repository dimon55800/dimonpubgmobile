package com.dimon.pubgtool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ToolApp() }
    }
}

@Composable
fun ToolApp() {
    var section by remember { mutableStateOf("Тренування прицілу") }
    var score by remember { mutableIntStateOf(0) }
    var recoil by remember { mutableFloatStateOf(0.35f) }
    val sections = listOf("Тренування прицілу", "ESP-полігон", "Контроль віддачі", "Радар ботів")

    MaterialTheme(colorScheme = darkColorScheme(
        primary = Color(0xFF8BE28B),
        background = Color(0xFF101510),
        surface = Color(0xFF1B241C)
    )) {
        Column(
            Modifier.fillMaxSize().background(Color(0xFF101510)).padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("DIMON PUBG TOOL", color = Color(0xFF9BE89B), fontSize = 23.sp, fontWeight = FontWeight.Black)
            Text("ANDROID TRAINING • OFFLINE", color = Color.LightGray, fontSize = 11.sp)
            Text("Оберіть тренувальний режим", color = Color.White, fontSize = 17.sp)
            sections.forEach { item ->
                Surface(
                    color = if (section == item) Color(0xFF304A32) else Color(0xFF1B241C),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().clickable { section = item }
                ) {
                    Text(item, Modifier.padding(15.dp), color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
            Surface(Modifier.fillMaxWidth().weight(1f), color = Color(0xFF1B241C), shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(section, color = Color(0xFF9BE89B), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    when (section) {
                        "Тренування прицілу" -> {
                            Text("Натискай на мішені, щоб набирати очки.", color = Color.White)
                            Box(Modifier.fillMaxWidth().weight(1f)) {
                                var targets by remember { mutableStateOf(List(4) { Offset(Random.nextFloat(), Random.nextFloat()) }) }
                                Canvas(Modifier.fillMaxSize()) {
                                    targets.forEach { p ->
                                        drawCircle(Color(0xFF9BE89B), 22.dp.toPx(), Offset(p.x * size.width, p.y * size.height))
                                        drawCircle(Color(0xFF18351C), 9.dp.toPx(), Offset(p.x * size.width, p.y * size.height))
                                    }
                                }
                                targets.forEachIndexed { i, p ->
                                    Box(Modifier.fillMaxSize().clickable {
                                        score += 1
                                        targets = targets.toMutableList().also {
                                            it[i] = Offset(Random.nextFloat(), Random.nextFloat())
                                        }
                                    })
                                }
                            }
                            Text("Влучання: $score", color = Color.White, fontSize = 16.sp)
                        }
                        "ESP-полігон" -> {
                            Text("Навчальна демонстрація позначення цілей у власному полігоні.", color = Color.White)
                            repeat(3) { i ->
                                Text("□ БОТ ${i + 1}  •  відстань: ${18 + i * 27} м", color = Color(0xFF9BE89B))
                            }
                        }
                        "Контроль віддачі" -> {
                            Text("Змінюй умовну силу віддачі та спостерігай за шкалою.", color = Color.White)
                            Slider(value = recoil, onValueChange = { recoil = it })
                            Text("Рівень: ${(recoil * 100).toInt()}%", color = Color.White)
                            Canvas(Modifier.fillMaxWidth().height(100.dp)) {
                                val x = size.width / 2
                                drawLine(Color(0xFF9BE89B), Offset(x, size.height), Offset(x + recoil * 70, 10f), 5f)
                            }
                        }
                        else -> {
                            Text("Мінікарта тестового полігону з ботами.", color = Color.White)
                            Canvas(Modifier.fillMaxWidth().weight(1f)) {
                                drawRect(Color(0xFF293B2A))
                                val pts = listOf(Offset(.22f,.3f), Offset(.72f,.24f), Offset(.61f,.72f), Offset(.35f,.8f))
                                pts.forEach {
                                    drawCircle(Color(0xFF9BE89B), 9.dp.toPx(), Offset(it.x * size.width, it.y * size.height))
                                }
                                drawCircle(Color.White, 7.dp.toPx(), Offset(size.width/2, size.height/2))
                            }
                        }
                    }
                }
            }
            Text("Це окремий офлайн-тренажер. Він не підключається до PUBG Mobile і не змінює гру.", color = Color.LightGray, fontSize = 11.sp)
        }
    }
}
