-- Lua script for sliding window rate limiting
-- KEYS[1]: a unique key identifying the resource (e.g., rate_limit:user:api)
-- ARGV[1]: max requests allowed in the window
-- ARGV[2]: window size in milliseconds
-- ARGV[3]: current timestamp in milliseconds (unique score/member for each request)

local key = KEYS[1]
local max_requests = tonumber(ARGV[1])
local window_millis = tonumber(ARGV[2])
local current_time = tonumber(ARGV[3])

-- 移除窗口之外的旧记录 (score < current_time - window_millis)
-- ZREMRANGEBYSCORE key min max
redis.call('ZREMRANGEBYSCORE', key, 0, current_time - window_millis)

-- 获取当前窗口内的请求数量
-- ZCARD key
local current_requests = redis.call('ZCARD', key)

-- 判断是否超过限制
if current_requests < max_requests then
  -- 未超限，记录当前请求 (member 和 score 都使用当前时间戳)
  -- ZADD key score member
  redis.call('ZADD', key, current_time, current_time)
  -- 设置/刷新 key 的过期时间，略大于窗口大小，防止冷数据无限增长
  -- EXPIRE key seconds
  redis.call('PEXPIRE', key, window_millis + 1000) -- Use PEXPIRE for milliseconds
  return 1 -- 允许访问
else
  -- 已超限
  return 0 -- 拒绝访问
end