# QuanLyChiTieu

## Thiết lập constain
Mấy anh vào object constain trong thư mục ultil
sửa cái BASE_URL lại nếu test trên local host
1. Mở git bash lên gõ ipconfig => ipv4 :  ví dụ 168.147.25.28
2. Sửa BASE_URL theo cú pháp http://[your_ip]:5000/api/
```kotlin
object Constant {
    val BASE_URL = "http://168.147.25.28:5000/api/"
}
