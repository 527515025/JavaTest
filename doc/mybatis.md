#mybatis

nternal error : nested exception is org.apache.ibatis.reflection.ReflectionException: There is no getter for property named 'langId' in 'class java.lang.Integer'

mybatis 有内置对象 _parameter ，对于单个参数的判断应该用 _parameter 代代替 例如：

```
<select id="getUsersWithOnLine" parameterType="int" resultMap="userMap">
		SELECT u.* FROM user u
		<where>
			<if test="_parameter != null">
				u.status != #{status}
			</if>
		</where>
	</select>
```

