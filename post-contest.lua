math.randomseed(os.time())
number = math.random(1, 100000000)
body = "{\n  \"slug\":" .. "\"" .. tostring(number) .."\"" .. ",\n  \"name\": \"Globo\",\n  \"description\": \"voting for the voice brazil 2015\",\n  \"start_date\": \"2015-06-20\",\n  \"end_date\": \"2016-01-01\"\n}"

print(body)

response = function()
  wrk.method = "POST"
  wrk.headers["Content-Type"] = "application/json"
  wrk.body = body
end
