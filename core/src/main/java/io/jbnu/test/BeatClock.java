package io.jbnu.test;

public class BeatClock {
    private float bpm;            // 분당 비트 수
    private float secPerBeat;     // 한 비트 사이 시간(초)
    private float elapsed;        // 누적 시간
    private int currentBeatIndex; // 현재 비트 번호

    public BeatClock(float bpm) {
        setBpm(bpm);
    }

    public void setBpm(float bpm) {
        this.bpm = bpm;
        this.secPerBeat = 60f / bpm; // 1비트 간격 계산
        this.elapsed = 0f;
        this.currentBeatIndex = -1;
    }

    public void update(float delta) {
        elapsed += delta;
    }

    public float getElapsed() {
        return elapsed;
    }

    public float getSecPerBeat() {
        return secPerBeat;
    }

    public float currentBeatF() {
        return elapsed / secPerBeat;
    }

    /**
     * 이번 프레임에서 새로운 비트가 시작되었는가?
     * (BPM 120이라면 0.5초마다 true 반환)
     */
    public boolean isNewBeat() {
        int beatIndex = (int) Math.floor(elapsed / secPerBeat);
        if (beatIndex != currentBeatIndex) {
            currentBeatIndex = beatIndex;
            return true;
        }
        return false;
    }

    public boolean isInBeatWindow(float windowSec) {
        float t = elapsed % secPerBeat;
        return t < windowSec || t > secPerBeat - windowSec;
    }
}
