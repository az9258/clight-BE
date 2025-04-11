**登入/注册接口**



# 需求

1. 实现注册和登入
2. 权限区分，体现在数据库记录
3. 实现token验证

# 数据库设计

*mongodb*

| _id          | username | email | password | created_at | updated_at       | role                 | status                   |
| ------------ | -------- | ----- | -------- | ---------- | ---------------- | -------------------- | ------------------------ |
| 用户唯一标识 | 用户名   | 邮箱  | 密码     | 创建时间   | 最后一次登入时间 | 用户权限admin/normal | 用户状态 0(封禁)/1(正常) |

# 功能

## 注册("/api/auth/register")

- 接收body(username, email, password)
- 验证数据是否合法
- 数据库验证用户是否存在(根据username来判断)
  - 存在则返回失败
- 存入数据
- 返回token和msg

## 登入("/api/auth/login")

- 接收body(username, password), token
- 验证token合法性
- 验证数据是否合法
- 数据库验证用户是否存在
- 返回msg



