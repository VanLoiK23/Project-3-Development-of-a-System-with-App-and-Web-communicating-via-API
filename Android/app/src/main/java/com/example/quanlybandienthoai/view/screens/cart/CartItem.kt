package com.example.quanlybandienthoai.view.screens.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.quanlybandienthoai.model.remote.entity.CartItem
import com.example.quanlybandienthoai.model.remote.entity.Phienbansanpham
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.view.screens.home.formatCurrency
import com.example.quanlybandienthoai.viewmodel.CartViewModel


@ExperimentalMaterial3Api
@Composable
fun CartItemCard(
    sp: Sanpham,
    pb: Phienbansanpham,
    cartItem: CartItem,
    viewModel: CartViewModel,
    navToProductPage: () -> Unit
) {
    val openRemoveCartItemDialog = remember { mutableStateOf(false) }

    Card(
        onClick = { navToProductPage() },
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, start = 5.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            FilledIconButton(
                onClick = { openRemoveCartItemDialog.value = true },
                modifier = Modifier.size(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Remove item button."
                )
            }
        }
        Row(
            Modifier
                .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                .height(IntrinsicSize.Min)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = cardColors(containerColor = Color.White)
            ) {
                val imageArray = sp.hinhanh.split(",").map { it.trim() }

                Image(
                    painter = rememberAsyncImagePainter(model = imageArray[0]),
                    contentDescription = "Cart Item Image",
                    Modifier
                        .size(80.dp)
                        .padding(5.dp)
                )
            }
            Column(
                Modifier
                    .padding(start = 10.dp, bottom = 5.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = sp.tensp,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )


                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("RAM: ")
                        }
                        append(pb.ram?.let { "$it GB" } ?: "N/A")
                        append(" | ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("ROM: ")
                        }
                        append(pb.rom?.let { "$it GB" } ?: "N/A")
                        append(" | ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Màu: ")
                        }
                        append(pb.mausac)
                    },
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 5.dp)
                )


                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatCurrency(pb.price_sale),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 5.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        FilledIconButton(
                            onClick = {
                                if (cartItem.soluong == 1) {
                                    openRemoveCartItemDialog.value = true
                                } else {
                                    viewModel.updateQty(cartItem, -1)
//                                    viewModel.updateSoluong(cartItem, -1)

//                                    viewModel.addToCart(sp, pb, -1)
                                }
                            },
                            modifier = Modifier.size(25.dp),
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.White)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Remove,
                                contentDescription = "Decrease item quantity button",
                                tint = Color.Black
                            )
                        }

                        Text(
                            text = "${cartItem.soluong}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        FilledIconButton(
                            onClick = {
                                viewModel.updateQty(cartItem, 1)
//                                viewModel.updateSoluong(cartItem, 1)
//                                viewModel.addToCart(sp, pb, 1)


                            },
                            modifier = Modifier.size(25.dp),
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = Color(
                                    0xFFCCFFFF
                                )
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Increase item quantity button",
                                tint = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }

    if (openRemoveCartItemDialog.value) {
        AlertDialog(
            onDismissRequest = { openRemoveCartItemDialog.value = false },
            title = {
                Text(text = "Xóa sản phẩm")
            },
            text = {
                Text("Bạn có muốn xóa sản phẩm ra khỏi giỏ hàng?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        openRemoveCartItemDialog.value = false
                        viewModel.deleteCartItem(cartItem.cart_id, cartItem.cart_item_id)
//                        viewModel.deletedCartItem(pb.maphienbansp)
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openRemoveCartItemDialog.value = false
                    }) {
                    Text("Cancel")
                }
            }
        )
    }
}