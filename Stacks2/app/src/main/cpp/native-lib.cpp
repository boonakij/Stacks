#include <jni.h>
#include <string>
#include <random>
#include <chrono>
#include <unistd.h>
#include <vector>

extern "C" JNIEXPORT jstring

JNICALL
Java_com_example_boon_stacks_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_boon_stacks_GameplayScene_getNewBaseValue(JNIEnv *env, jobject instance, jint baseValue, jint blockValue) {
    if (baseValue % blockValue == 0){
        return baseValue/blockValue;
    }
    else {
        return baseValue + blockValue;
    }
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_boon_stacks_GameplayScene_addValues(JNIEnv *env, jobject instance, jint blockValue1, jint blockValue2) {
    return blockValue1 + blockValue2;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_boon_stacks_GameplayScene_createNewValue(JNIEnv *env, jobject instance,
                                                          jintArray baseValues_,
                                                          jintArray blockValues_) {
    jint *baseValues = env->GetIntArrayElements(baseValues_, NULL);
    jint *blockValues = env->GetIntArrayElements(blockValues_, NULL);

    jsize baseLen = env->GetArrayLength(baseValues_);
    jsize blockLen = env->GetArrayLength(blockValues_);

    uint64_t seed = std::chrono::high_resolution_clock::now().time_since_epoch().count();	// seed
    std::mt19937 gen(seed); // random number generator
    std::uniform_int_distribution<> dist(1, 20); // uniform distribution
    std::uniform_int_distribution<> dist0(0, 10); // uniform distribution
    std::uniform_int_distribution<> dist1(0, baseLen - 1); // uniform distribution

    int createdValue = 1;

    if (dist0(gen) < 2) {
        int randomBaseValue = baseValues[dist1(gen)];
        std::vector<int> valueFactors;
        for (int i = 1; i <= randomBaseValue; ++i) {
            if (randomBaseValue % i == 0) {
                valueFactors.push_back(i);
            }
        }

        std::uniform_int_distribution<> dist2(0, valueFactors.size() - 1); // uniform distribution
        createdValue = valueFactors.at(dist2(gen));

        std::uniform_int_distribution<> dist3(0, 3); // uniform distribution
        if (dist3(gen) == 0) {
            std::uniform_int_distribution<> dist4(0, blockLen); // uniform distribution
            int randomBlockValue = blockValues[dist4(gen)];
            if (randomBlockValue < createdValue) {
                createdValue -= randomBlockValue;
            }
        }
    }
    else {
        createdValue = dist(gen);
    }

    env->ReleaseIntArrayElements(baseValues_, baseValues, 0);
    env->ReleaseIntArrayElements(blockValues_, blockValues, 0);

    return createdValue;
}