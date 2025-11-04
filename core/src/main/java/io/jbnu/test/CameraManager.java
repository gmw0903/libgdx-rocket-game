package io.jbnu.test;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CameraManager {
    private float shakeDuration = 0f;
    private float shakeTimer = 0f;
    private float shakeIntensity = 0f;
    private Vector2 originalPosition = new Vector2(); // 카메라의 원래 위치
    public boolean isShakeStarted;
    private Camera camera;

    public  CameraManager(Camera camera){
        this.camera = camera;
        isShakeStarted = false;
    }

    // 카메라 흔들림 시작 함수
    public void startShake(float duration, float intensity) {
        if (shakeDuration > 0) return; // 이미 흔들리고 있다면 무시

        this.shakeDuration = duration;
        this.shakeTimer = 0f;
        this.shakeIntensity = intensity;

        // ⭐ 중요: 흔들림 시작 전 카메라의 원래 위치를 저장
        originalPosition.set(camera.position.x,camera.position.y);
        isShakeStarted = true;
    }

    public void updateShake(float delta) {

        if (shakeTimer < shakeDuration) {
            shakeTimer += delta;
            // 1. 흔들림 강도 감쇠 (시간이 지날수록 진동을 줄여 자연스럽게 만듦)
            // '감쇠 비율'은 0 (시작)에서 1 (종료) 사이의 값.
            float decayFactor = shakeTimer / shakeDuration;
            float currentIntensity = shakeIntensity * (1f - decayFactor);
            // 2. 랜덤 오프셋 계산 (X, Y축)
            float offsetX = MathUtils.random(-1f, 1f) * currentIntensity;
            float offsetY = MathUtils.random(-1f, 1f) * currentIntensity;
            // 3. 카메라 위치 적용
            // 저장된 원래 위치를 기준으로 오프셋을 더합니다.
            camera.position.x = originalPosition.x + offsetX;
            camera.position.y = originalPosition.y + offsetY;
        } else if (shakeDuration > 0) {
            // 4. 흔들림 종료: 카메라를 원래 위치로 복원하고 변수 초기화
            camera.position.set(originalPosition,0);
            shakeDuration = 0f;
            shakeTimer = 0f;
            isShakeStarted = false;
            // 카메라 업데이트는 GameWorld/GameScreen의 render 루프에서 처리
        }
    }



}
