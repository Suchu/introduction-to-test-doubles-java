import com.techreturners.bubbleteaordersystem.model.*;
import com.techreturners.bubbleteaordersystem.service.BubbleTeaMessenger;
import com.techreturners.bubbleteaordersystem.service.BubbleTeaOrderService;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import testhelper.DummySimpleLogger;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class BubbleTeaOrderServiceTest {

    private DebitCard testDebitCard;
    private PaymentDetails paymentDetails;
    private DummySimpleLogger dummySimpleLogger;
    private BubbleTeaMessenger mockMessenger;
    private BubbleTeaOrderService bubbleTeaOrderService;

    @Parameterized.Parameter()
    public String bubbleTeaType;
    @Parameterized.Parameter(1)
    public double teaPrice;

    @Parameterized.Parameters(name = "{index}: bubbleTeaType = {0}, teaPrice = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"MatchaMilkTea", 6.78},
                {"PeachIceTea", 4.20},
                {"LycheeIceTea", 4.50},
                {"JasmineMilkTea", 5.15},
                {"OolongMilkTea", 4.50},
        });
    }

    @BeforeEach
    public void setup() {
        DebitCard testDebitCard = new DebitCard("0123456789");
        paymentDetails = new PaymentDetails("hello kitty", "sanrio puroland", testDebitCard);
        DummySimpleLogger dummySimpleLogger = new DummySimpleLogger();
        mockMessenger = mock(BubbleTeaMessenger.class);
        bubbleTeaOrderService = new BubbleTeaOrderService(dummySimpleLogger, mockMessenger);
    }

    @Test
    public void shouldCreateBubbleTeaOrderRequestWhenCreateOrderRequestIsCalled() {

        //Arrange
        BubbleTea bubbleTea = new BubbleTea(BubbleTeaTypeEnum.valueOf(bubbleTeaType), teaPrice);
        System.out.println(bubbleTeaType + teaPrice);
        BubbleTeaRequest bubbleTeaRequest = new BubbleTeaRequest(paymentDetails, bubbleTea);

        BubbleTeaOrderRequest expectedResult = new BubbleTeaOrderRequest(
                "hello kitty",
                "sanrio puroland",
                "0123456789",
                BubbleTeaTypeEnum.valueOf(bubbleTeaType)
        );

        //Act
        BubbleTeaOrderRequest result = bubbleTeaOrderService.createOrderRequest(bubbleTeaRequest);
//        System.out.println(result);

        //Assert
        assertEquals(expectedResult.getName(), result.getName());
        assertEquals(expectedResult.getAddress(), result.getAddress());
        assertEquals(expectedResult.getDebitCardDigits(), result.getDebitCardDigits());
        assertEquals(expectedResult.getBubbleTeaType(), result.getBubbleTeaType());

        //Verify Mock was called with the BubbleTeaOrderRequest result object
        verify(mockMessenger).sendBubbleTeaOrderRequestEmail(result);
        verify(mockMessenger, times(1)).sendBubbleTeaOrderRequestEmail(result);
    }

}
