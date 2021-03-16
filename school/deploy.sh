

# 发送
echo "发送...."
scp -r ./target/blade-api.jar root@8.136.115.217:/myProject &&
# 重启App
echo "重启...."
ssh root@8.136.115.217 "cd /myProject;./service.sh restart;./service.sh log;"

