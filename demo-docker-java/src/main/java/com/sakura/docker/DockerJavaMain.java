package com.sakura.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;

import java.net.URI;
import java.util.List;


public class DockerJavaMain {

//    private static final Logger dockerLogger = (Logger) LoggerFactory.getLogger("com.github.dockerjava");
//    private static final Logger clientLogger = (Logger) LoggerFactory.getLogger("org.apache.hc.client5.http");

    public static void main(String[] args) {
//        dockerLogger.setLevel(Level.INFO);  // 动态设置为 DEBUG
//        clientLogger.setLevel(Level.INFO);
        // 配置 Docker 客户端
        ApacheDockerHttpClient httpClient = new ApacheDockerHttpClient.Builder().dockerHost(URI.create("npipe:////./pipe/docker_engine")).build();

        try (DockerClient client = DockerClientBuilder.getInstance().withDockerHttpClient(httpClient).build()) {

            // 检查连接
//            PingCmd pingCmd = client.pingCmd();
//            pingCmd.exec();

            // 拉取镜像
            String image = "nginx:latest";
//            PullImageCmd pullImageCmd = client.pullImageCmd(image);
//            PullImageResultCallback imageResultCallback = new PullImageResultCallback() {
//                @Override
//                public void onNext(PullResponseItem item) {
//                    // 监控 拉取镜像的进度
//                    System.out.println("pull image: " + item.getStatus());
//                    super.onNext(item);
//                }
//            };
//            pullImageCmd
//                    .exec(imageResultCallback) // 执行命令
//                    .awaitCompletion(); // 等待结果

            // 创建容器
            CreateContainerCmd containerCmd = client.createContainerCmd(image);
            CreateContainerResponse createContainerResponse = containerCmd.withName("docker-nginx") // 指定名字
                    .withHostConfig(new HostConfig().withPortBindings(PortBinding.parse("8800:80")) // 端口映射
                            .withNetworkMode("bridge")) // 设置网络
                    .withAttachStdout(false).withAttachStderr(false).withTty(true).withCmd("echo", "Hello Docker").exec();
            String containerId = createContainerResponse.getId();
            System.out.println("运行容器的id：" + containerId);

            // 运行容器
            client.startContainerCmd(containerId).exec();

            // 查看容器状态
            ListContainersCmd listContainersCmd = client.listContainersCmd();
            List<Container> containerList = listContainersCmd.withShowAll(true).exec();
            for (Container container : containerList) {
                System.out.println(container);
            }

            // 查看日志
            LogContainerResultCallback logContainerResultCallback = new LogContainerResultCallback() {
                @Override
                public void onNext(Frame item) {
                    System.out.println(item.getStreamType());
                    System.out.println("日志：" + new String(item.getPayload()));
                    super.onNext(item);
                }
            };

            // 阻塞等待日志输出
            client.logContainerCmd(containerId)
                    .withStdErr(true)
                    .withStdOut(true)
                    .exec(logContainerResultCallback)
                    .awaitCompletion();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
