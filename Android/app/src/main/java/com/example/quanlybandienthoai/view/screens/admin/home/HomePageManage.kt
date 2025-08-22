package com.example.quanlybandienthoai.view.screens.admin.home

import android.content.Context
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.common.utils.DataUtils.getColorPaletteList
import co.yml.charts.ui.barchart.GroupBarChart
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarPlotData
import co.yml.charts.ui.barchart.models.GroupBar
import co.yml.charts.ui.barchart.models.GroupBarChartData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.quanlybandienthoai.model.remote.entity.PnPxByMonth
import com.example.quanlybandienthoai.model.remote.entity.PnPxByQuarter
import com.example.quanlybandienthoai.model.remote.entity.RevenueOrderFollowWeekOrMonth
import com.example.quanlybandienthoai.model.remote.entity.RevenueProductTopSelling
import com.example.quanlybandienthoai.model.remote.entity.RevenuePurchaseFollowWeekOrMonth
import com.example.quanlybandienthoai.view.components.BottomNavigationBar
import com.example.quanlybandienthoai.view.screens.admin.sanpham.SearchTopBar
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.view.screens.home.formatCurrency
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.admin.HomeManageViewModel
import kotlin.math.abs
import kotlin.math.roundToInt


@Composable
fun DashboardScreen(
    navController: NavController,
    homeManageViewModel: HomeManageViewModel = viewModel(LocalContext.current as ComponentActivity),
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Đơn hàng", "Doanh thu", "Nhập xuất hàng")

    val orders by homeManageViewModel.revenueOrders.collectAsState()

    val purchases by homeManageViewModel.revenuePurchases.collectAsState()

    val products by homeManageViewModel.revenueProductTops.collectAsState()

    val dataPurchaseYear by homeManageViewModel.revenuePnPxMonth.collectAsState()

    val dataPurchaseQuarter by homeManageViewModel.revenuePnPxQuarter.collectAsState()

    LaunchedEffect(Unit) {
        homeManageViewModel.getInfoRevenueOrder("week")
        homeManageViewModel.getInfoRevenuePurchase("week")
        homeManageViewModel.getInfoGeneral()
        homeManageViewModel.getInfoProductTopSelling()
        homeManageViewModel.getInfoPnPxFollowYear()
        homeManageViewModel.getInfoPnPxFollowQuarter()

        mainViewModel.updateSelectedScreen(navController)
    }

    androidx.compose.material3.Scaffold(
        topBar = {
            HomeTopBar{
                navController.navigate("login") {
                    popUpTo(0)
                }

                appViewModel.logOut()
            }
        },
        bottomBar = {
            BottomNavigationBar(navController, mainViewModel)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F2F7))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Text("Xin chào "+ (appViewModel.getCurrentUser()?.firstName ?: ""), fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = fontFamily())

            Spacer(modifier = Modifier.height(16.dp))

            // Info cards
            DashboardStats(homeManageViewModel)

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                contentColor = Color.Black,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                title,
                                color = if (selectedTabIndex == index) Color(0xFF9C27B0) else Color.Gray
                            )
                        })
                }
            }
            // Chart based on selected tab
            val valueOrders = orders.mapNotNull { (it as? RevenueOrderFollowWeekOrMonth)?.count }

            when (selectedTabIndex) {
                0 -> WeeklyLineChartWithSelector(
                    if (orders.isNotEmpty()) {
                        valueOrders
                    } else {
                        listOf(0, 0, 0, 0, 0, 0, 0)
                    }, "order", homeManageViewModel
                )

                1 -> WeeklyLineChartWithSelector(if (purchases.isNotEmpty()) {
                    purchases.mapNotNull { (it as? RevenuePurchaseFollowWeekOrMonth)?.total?.toInt() }
                } else {
                    listOf(0, 0, 0, 0, 0, 0, 0)
                }, "purchase", homeManageViewModel)

                2 -> dataPurchaseYear?.let {
                    dataPurchaseQuarter?.let { it1 ->
                        MonthlyQuarterLineChartWithSelector(
                            it,
                            it1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //Product Pie chart
            if (products.isNotEmpty()) {
                PieChartTopProductSelling(products, LocalContext.current)
            }
        }
    }
}


@Composable
fun DashboardStats(homeManageViewModel: HomeManageViewModel) {
    val stats = listOf(
        Triple("Kho khả dụng", homeManageViewModel.quantityWareHouse, "+11.01%"),
        Triple("Số lượng khách hàng", homeManageViewModel.quantityUser, "-0.03%"),
        Triple("Số lượng sản phẩm", homeManageViewModel.quantityProduct, "+15.03%"),
        Triple("Số lượng giao dịch", homeManageViewModel.quantityPurchase, "+6.08%")
    )

    val colors = listOf(
        Brush.verticalGradient(listOf(Color(0xFF0D6EFD), Color(0xFF70B9FF))), // Blue
        Brush.verticalGradient(listOf(Color(0xFF343A40), Color(0xFF6C757D))), // Dark gray
        Brush.verticalGradient(listOf(Color(0xFF198754), Color(0xFF7DD3A4))), // Green
        Brush.verticalGradient(listOf(Color(0xFF0D6EFD), Color(0xFF70B9FF)))  // Blue again
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = false
    ) {
        stats.forEachIndexed { index, (title, value) ->
            item {
                Box(
                    modifier = Modifier
                        .background(
                            brush = colors[index], shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Column {
                        Text(text = title, color = Color.White, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = value.toString(),
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
//                            val isPositive = change.contains("+")
//                            Icon(
//                                imageVector = if (isPositive) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
//                                contentDescription = null,
//                                tint = if (isPositive) Color.Green else Color.Red,
//                                modifier = Modifier.size(16.dp)
//                            )
//                            Text(
//                                text = change,
//                                color = if (isPositive) Color.Green else Color.Red,
//                                fontSize = 12.sp,
//                                fontWeight = FontWeight.Medium
//                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyLineChartWithSelector(
    revenueTotal: List<Int>, type: String, homeManageViewModel: HomeManageViewModel
) {
    var selectedTimeRange by remember { mutableStateOf("Tuần") }
    val timeOptions = listOf("Tuần", "Tháng")

    val typeSelect by remember(selectedTimeRange) {
        derivedStateOf {
            if (selectedTimeRange == "Tuần") "week" else "month"
        }
    }

    LaunchedEffect(typeSelect, type) {
        when (type) {
            "order" -> homeManageViewModel.getInfoRevenueOrder(typeSelect)
            "purchase" -> homeManageViewModel.getInfoRevenuePurchase(typeSelect)
        }
    }

    Column {
        // Selector
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            timeOptions.forEach { option ->
                val isSelected = selectedTimeRange == option
                Text(text = option,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) Color(0xFF9C27B0) else Color.LightGray)
                        .clickable { selectedTimeRange = option }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    color = if (isSelected) Color.White else Color.Black,
                    fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }


        // Chart with animation
        Crossfade(
            targetState = selectedTimeRange,
            label = "ChartFade",
            modifier = Modifier.fillMaxWidth()
        ) { time ->
            when (time) {
                "Tuần" -> WeeklyRevenueLineChart(revenueTotal, type)
                "Tháng" -> MonthlyRevenueLineChart(revenueTotal, type)
            }
        }
    }
}

@Composable
fun MonthlyQuarterLineChartWithSelector(
    dataMonth: PnPxByMonth, dataQuarter: PnPxByQuarter
) {
    var selectedTimeRange by remember { mutableStateOf("Năm") }
    val timeOptions = listOf("Năm", "Quý")


    Column {
        // Selector
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            timeOptions.forEach { option ->
                val isSelected = selectedTimeRange == option
                Text(text = option,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) Color(0xFF9C27B0) else Color.LightGray)
                        .clickable { selectedTimeRange = option }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    color = if (isSelected) Color.White else Color.Black,
                    fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }


        // Chart with animation
        Crossfade(
            targetState = selectedTimeRange,
            label = "ChartFade",
            modifier = Modifier.fillMaxWidth()
        ) { time ->
            when (time) {
                "Năm" -> GroupBarChartPnPx(convertToTransactionYearList(dataMonth))
                "Quý" -> GroupBarChartPnPx(convertToTransactionQuarterList(dataQuarter))
            }
        }
    }
}

@Composable
fun WeeklyRevenueLineChart(revenueTotal: List<Int>, type: String) {
    val daysOfWeekIcons = listOf("CCN", "T2", "T3", "T4", "T5", "T6", "T7")
//    val ordersPerDay = listOf(12f, 24f, 28f, 36f, 16f, 22f, 10f)
    val ordersPerDay = revenueTotal.map { it.toFloat() }
    var lastOffsetX by remember { mutableStateOf(0f) }

// Dữ liệu điểm không cần dịch
    val pointsData = ordersPerDay.mapIndexed { index, value ->
        Point((index).toFloat(), value)
    }
    val ySteps = 4

// Trục X: 6 khoảng tương ứng với 7 điểm → steps = 6
    val xAxisData = AxisData.Builder().axisStepSize(55.dp).backgroundColor(Color.Transparent)
        .steps(ordersPerDay.size - 1) // 6 khoảng
        .labelData { i -> daysOfWeekIcons.getOrElse(i) { "" } } // Đảm bảo không crash
        .labelAndAxisLinePadding(15.dp).axisLabelColor(Color.Gray).axisLineColor(Color.LightGray)
        .build()


    val yAxisData = AxisData.Builder().steps(ySteps).backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(10.dp).labelData { i ->
//            val maxY = 40
//            val scale = maxY / ySteps
//            (i * scale).toString()
            ""
        }.axisLabelColor(Color.Gray).axisLineColor(Color.LightGray).build()

    var chartWidth by remember { mutableStateOf(0f) }


    val listLine = listOf(Line(dataPoints = pointsData, // Lưu toàn bộ danh sách điểm
        lineStyle = LineStyle(color = Color(0xFF9C27B0), width = 3f),
        intersectionPoint = IntersectionPoint(
            color = Color.White,
            radius = 6.dp,
            draw = { offset ->
                drawCircle(
                    color = Color.White, radius = 6.dp.toPx(), center = offset
                )

                Log.d("BBB", "Offset X: ${offset.x}")
//                    val index =
//                        pointsData.indexOfFirst { it.x.toInt() == (offset.x / 144.375).toInt() }
                val stepSize = (offset.x.toFloat() - lastOffsetX.toFloat()).takeIf { it > 0 }
                    ?: 96.25f // Giá trị mặc định nếu chưa có
                lastOffsetX = offset.x // Cập nhật giá trị
                val index =
                    pointsData.indexOfFirst { it.x.toInt() == (offset.x / stepSize).toInt() }
//                                        val index =
//                        pointsData.indexOfFirst { it.x.toInt() == (offset.x / chartWidth).toInt() }
                Log.d(
                    "DEBUG",
                    "ChartWidth: $chartWidth, StepSize: ${chartWidth / (pointsData.size - 1)}"
                )
                Log.d("BBB", "Width X: ${stepSize}")

//                    val stepSize = 96.25 / (pointsData.size - 1)
//                    val index = pointsData.indexOfFirst { it.x.toInt() == (offset.x / stepSize).roundToInt() }
//                    val closestPoint = pointsData.minByOrNull { abs(it.x * stepSize - offset.x) }
//                    val index = pointsData.indexOf(closestPoint)
                val adjustedOffsetX = if (index == 0) {
                    offset.x + 12.dp.toPx() // Dịch sang phải nếu là cột đầu tiên
                } else {
                    offset.x
                }
                if (index != -1) {
                    val point = pointsData[index]
                    val orderValue = if (type == "purchase") {
                        formatCurrency(point.y.toDouble())
                    } else {
                        "${point.y.toInt()} đơn"
                    }

                    drawContext.canvas.nativeCanvas.drawText(orderValue, if (type == "purchase") {
                        adjustedOffsetX + 13.dp.toPx()
                    } else {
                        adjustedOffsetX + 10.dp.toPx()
                    }, offset.y - 16.dp.toPx(), Paint().apply {
                        textAlign = Paint.Align.CENTER
                        textSize = if (stepSize == 96.25f) {
                            30f
                        } else {
                            36f
                        }
                        color = android.graphics.Color.BLACK
                        isFakeBoldText = true
                        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
                    })
                }

//                    val index = pointsData.indexOfFirst { abs(it.x * stepSize - offset.x) < stepSize / 2 }
                Log.d("DEBUG", "Offset X: ${offset.x}, Calculated Index: $index")
//                    val closestPoint = pointsData.minByOrNull { abs(it.x * stepSize - offset.x) }
//                    closestPoint?.let { point ->
//                        val orderValue = "${point.y.toInt()} đơn"
//
//                        drawContext.canvas.nativeCanvas.drawText(
//                            orderValue,
//                            offset.x,
//                            offset.y - 24.dp.toPx(), // Điều chỉnh vị trí để tránh chồng lên chấm trắng
//                            android.graphics.Paint().apply {
//                                textAlign = android.graphics.Paint.Align.CENTER
//                                textSize = 36f
//                                color = android.graphics.Color.BLACK
//                                isFakeBoldText = true
//                            }
//                        )
//                    }

//                    val closestPoint = pointsData.minByOrNull { abs(it.x * stepSize - offset.x) }
//                    closestPoint?.let { point ->
//                        val orderValue = "${point.y.toInt()} đơn"
//
//                        drawContext.canvas.nativeCanvas.drawText(
//                            orderValue,
//                            point.x * stepSize, // Sử dụng vị trí tính toán chính xác theo điểm dữ liệu
//                            offset.y - 24.dp.toPx(),
//                            android.graphics.Paint().apply {
//                                textAlign = android.graphics.Paint.Align.CENTER
//                                textSize = 36f
//                                color = android.graphics.Color.BLACK
//                                isFakeBoldText = true
//                            }
//                        )
//                    }
            }),
        shadowUnderLine = ShadowUnderLine(
            color = Color(0xFF9C27B0), alpha = 0.1f
        ),
        selectionHighlightPopUp = SelectionHighlightPopUp { offset, point ->
            drawContext.canvas.nativeCanvas.drawText(if (type == "purchase") {
                formatCurrency(point.y.toDouble())
            } else {
                "${point.y.toInt()} đơn"
            }, if (type == "purchase") {
                offset.x + 20.dp.toPx()
            } else {
                offset.x + 17.dp.toPx()
            }, offset.y + 24.dp.toPx(), Paint().apply {
                textAlign = Paint.Align.CENTER
                textSize = 26f
                color = android.graphics.Color.parseColor("#FFD700")
                isFakeBoldText = true
            })
        },
        selectionHighlightPoint = SelectionHighlightPoint { startOffset, endOffset ->
            drawContext.canvas.nativeCanvas.drawLine(endOffset.x,
                endOffset.y, // Bắt đầu từ chính điểm click trên trục X
                endOffset.x,
                startOffset.y,  // Kết thúc tại điểm giao
                Paint().apply {
                    color = android.graphics.Color.parseColor("#00A86B")
                    strokeWidth = 2f
                    pathEffect =
                        DashPathEffect(floatArrayOf(10f, 10f), 0f) // Nét đứt
                })
        }

    ))

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listLine
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = Color.Transparent),
        backgroundColor = Color.White
    )

    LineChart(modifier = Modifier
        .fillMaxWidth()
        .height(350.dp)
        .padding(0.dp)
        .clip(RoundedCornerShape(16.dp))
        .onSizeChanged { size ->
            chartWidth = size.width.toFloat() // Cập nhật giá trị chartWidth
        }
        .background(Color(0xFFF8F8F8)), lineChartData = lineChartData)
}


@Composable
fun MonthlyRevenueLineChart(revenueTotal: List<Int>, type: String) {
    val daysOfWeekIcons = listOf("TuasaTuần 1", "Tuần 2", "Tuần 3", "Tuần 4", "Tuần 5")

    val ordersPerDay = revenueTotal.map { it.toFloat() }
    var lastOffsetX by remember { mutableStateOf(0f) }

// Dữ liệu điểm không cần dịch
    val pointsData = ordersPerDay.mapIndexed { index, value ->
        Point((index).toFloat(), value)
    }
    val ySteps = 4

    val xAxisData = AxisData.Builder().axisStepSize(85.dp).backgroundColor(Color.Transparent)
        .steps(ordersPerDay.size - 1).labelData { i -> daysOfWeekIcons.getOrElse(i) { "" } }
        .labelAndAxisLinePadding(15.dp).axisLabelColor(Color.Gray).axisLineColor(Color.LightGray)
        .build()


    val yAxisData = AxisData.Builder().steps(ySteps).backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(10.dp).labelData { i ->
            ""
        }.axisLabelColor(Color.Gray).axisLineColor(Color.LightGray).build()

    var chartWidth by remember { mutableStateOf(0f) }


    val listLine = listOf(Line(dataPoints = pointsData,
        lineStyle = LineStyle(color = Color(0xFF9C27B0), width = 3f),
        intersectionPoint = IntersectionPoint(
            color = Color.White,
            radius = 6.dp,
            draw = { offset ->
                drawCircle(
                    color = Color.White, radius = 6.dp.toPx(), center = offset
                )

                Log.d("BBB", "Offset X: ${offset.x}")
                val stepSize = (offset.x.toFloat() - lastOffsetX.toFloat()).takeIf { it > 0 }
                    ?: 96.25f // Giá trị mặc định nếu chưa có
                lastOffsetX = offset.x // Cập nhật giá trị
                val index =
                    pointsData.indexOfFirst { it.x.toInt() == (offset.x / stepSize).toInt() }

                Log.d(
                    "DEBUG",
                    "ChartWidth: $chartWidth, StepSize: ${chartWidth / (pointsData.size - 1)}"
                )
                Log.d("BBB", "Width X: ${stepSize}")

                val adjustedOffsetX = if (index == 0) {
                    offset.x + 12.dp.toPx()
                } else {
                    offset.x
                }
                if (index != -1) {
                    val point = pointsData[index]
                    val orderValue = if (type == "purchase") {
                        formatCurrency(point.y.toDouble())
                    } else {
                        "${point.y.toInt()} đơn"
                    }

                    drawContext.canvas.nativeCanvas.drawText(orderValue, if (type == "purchase") {
                        if (index == 0) {
                            adjustedOffsetX + 40.dp.toPx()
                        } else if (index == 4) {
                            adjustedOffsetX - 25.dp.toPx()
                        } else {
                            adjustedOffsetX + 19.dp.toPx()
                        }
                    } else {
                        if (index == 4) {
                            adjustedOffsetX - 5.dp.toPx()
                        } else {
                            adjustedOffsetX + 15.dp.toPx()
                        }
                    }, offset.y - 16.dp.toPx(), Paint().apply {
                        textAlign = Paint.Align.CENTER
                        textSize = if (stepSize == 96.25f) {
                            27f
                        } else {
                            33f
                        }
                        color = android.graphics.Color.BLACK
                        isFakeBoldText = true
                        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
                    })
                }
            }),
        shadowUnderLine = ShadowUnderLine(
            color = Color(0xFF9C27B0), alpha = 0.1f
        ),
        selectionHighlightPopUp = SelectionHighlightPopUp { offset, point ->
            drawContext.canvas.nativeCanvas.drawText(if (type == "purchase") {
                formatCurrency(point.y.toDouble())
            } else {
                "${point.y.toInt()} đơn"
            }, if (type == "purchase") {
                if (point.y.toInt() == revenueTotal[0]) {
                    offset.x + 55.dp.toPx()
                } else if (point.y.toInt() == revenueTotal[4]) {
                    offset.x - 10.dp.toPx()
                } else {
                    offset.x + 27.dp.toPx()
                }
            } else {
                if (point.y.toInt() == revenueTotal[4]) {
                    offset.x - 5.dp.toPx()
                } else {
                    offset.x + 20.dp.toPx()
                }
            }, offset.y + 24.dp.toPx(), Paint().apply {
                textAlign = Paint.Align.CENTER
                textSize = 26f
                color = android.graphics.Color.parseColor("#FFD700")
                isFakeBoldText = true
            })
        },
        selectionHighlightPoint = SelectionHighlightPoint { startOffset, endOffset ->
            drawContext.canvas.nativeCanvas.drawLine(endOffset.x,
                endOffset.y, // Bắt đầu từ chính điểm click trên trục X
                endOffset.x,
                startOffset.y,  // Kết thúc tại điểm giao
                Paint().apply {
                    color = android.graphics.Color.parseColor("#00A86B")
                    strokeWidth = 2f
                    pathEffect =
                        DashPathEffect(floatArrayOf(10f, 10f), 0f) // Nét đứt
                })
        }

    ))

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listLine
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = Color.Transparent),
        backgroundColor = Color.White
    )

    LineChart(modifier = Modifier
        .fillMaxWidth()
        .height(350.dp)
        .padding(0.dp)
        .clip(RoundedCornerShape(16.dp))
        .onSizeChanged { size ->
            chartWidth = size.width.toFloat() // Cập nhật giá trị chartWidth
        }
        .background(Color(0xFFF8F8F8)), lineChartData = lineChartData)

}

data class ProductTransaction(
    val label: String, // Nhãn hiển thị (Nhập hàng, Xuất hàng)
    val importQuantity: Float, // Số lượng nhập
    val exportQuantity: Float // Số lượng xuất
)

fun convertToTransactionYearList(pnPxData: PnPxByMonth): List<ProductTransaction> {
    return (1..12).map { month ->
        ProductTransaction(
            "Tháng $month",
            pnPxData.phieunhap[month]?.toFloat() ?: 0f, // Lấy giá trị nhập, mặc định là 0
            pnPxData.phieuxuat[month]?.toFloat() ?: 0f  // Lấy giá trị xuất, mặc định là 0
        )
    }
}

fun convertToTransactionQuarterList(pnPxData: PnPxByQuarter): List<ProductTransaction> {
    return (1..4).map { quarter ->
        ProductTransaction(
            "Quý $quarter",
            pnPxData.phieunhap[quarter]?.toFloat() ?: 0f, // Lấy giá trị nhập, mặc định là 0
            pnPxData.phieuxuat[quarter]?.toFloat() ?: 0f  // Lấy giá trị xuất, mặc định là 0
        )
    }
}


@Composable
fun GroupBarChartPnPx(transactionData: List<ProductTransaction>) {
    val groupBarData = transactionData.map { transaction ->
        GroupBar(
            label = transaction.label,
            barList = listOf(
                BarData(Point(1f, transaction.importQuantity), Color.Red, "Nhập"),
                BarData(Point(2f, transaction.exportQuantity), Color.Blue, "Xuất")
            )
        )
    }

    val groupBarPlotData = BarPlotData(
        groupBarList = groupBarData,
        barColorPaletteList = listOf( Color(0xFFEF5350),Color(0xFF42A5F5)) // Xanh xuat, đỏ nhap
    )

    val xAxisData = AxisData.Builder()
        .axisStepSize(50.dp) // Khoảng cách giữa các nhóm
        .steps(groupBarData.size - 1)
        .bottomPadding(40.dp)
        .labelData { index -> groupBarData[index].label }
        .build()
    val maxValue = transactionData.maxOf { maxOf(it.importQuantity, it.exportQuantity) }

    val yAxisData = AxisData.Builder()
        .steps(6) // Chia nhỏ để hiển thị tốt hơn
        .labelAndAxisLinePadding(10.dp)
        .axisOffset(20.dp)
        .labelData { index -> formatCurrency((index * (maxValue / 6)).toDouble()) }
        .build()
//    val yAxisData = AxisData.Builder()
//        .steps(5)
//        .labelAndAxisLinePadding(20.dp)
//        .axisOffset(20.dp)
//        .labelData { index -> (index * 50).toString() }
//        .build()

    val groupBarChartData = GroupBarChartData(
        barPlotData = groupBarPlotData,
        xAxisData = xAxisData,
        yAxisData = yAxisData
    )

    GroupBarChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(5.dp),
        groupBarChartData = groupBarChartData
    )
}

@Composable
fun PieChartTopProductSelling(productRevenue: List<RevenueProductTopSelling>, context: Context) {

    val pieChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice(
                productRevenue[0].name,
                productRevenue[0].soluongban.toFloat(),
                Color(0xFFEF5350)
            ), // Đỏ sáng
            PieChartData.Slice(
                productRevenue[1].name,
                productRevenue[1].soluongban.toFloat(),
                Color(0xFFFFA726)
            ), // Cam
            PieChartData.Slice(
                productRevenue[2].name,
                productRevenue[2].soluongban.toFloat(),
                Color(0xFF66BB6A)
            ), // Xanh lá
            PieChartData.Slice(
                productRevenue[3].name,
                productRevenue[3].soluongban.toFloat(),
                Color(0xFF42A5F5)
            ), // Xanh dương
            PieChartData.Slice(
                productRevenue[4].name,
                productRevenue[4].soluongban.toFloat(),
                Color(0xFFAB47BC)
            )  // Tím
        ),
        plotType = PlotType.Pie
    )

    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        showSliceLabels = false, // Ẩn nhãn mặc định để hiển thị khi click
        animationDuration = 1500,
        sliceLabelTextSize = 18.sp,
        sliceLabelTextColor = Color.Black,
        sliceLabelTypeface = Typeface.DEFAULT_BOLD,
        backgroundColor = Color.White,
        sliceLabelEllipsizeAt = TextUtils.TruncateAt.MIDDLE,
    )

    Column(modifier = Modifier.height(500.dp)) {
        Spacer(modifier = Modifier.height(20.dp))
        Legends(legendsConfig = DataUtils.getLegendsConfigFromPieChartData(pieChartData, 3))
        PieChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp).padding(16.dp),
            pieChartData,
            pieChartConfig
        ) { slice ->
            Toast.makeText(
                context,
                "Số lượng bán của sản phẩm " + slice.label + " là " + slice.value.toInt(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}

